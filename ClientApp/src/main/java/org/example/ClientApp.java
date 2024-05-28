package org.example;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import servicesf.*;
import servicesg.*;

public class ClientApp {

    private final static Logger logger = Logger.getLogger(ClientApp.class.getName());
    static String cfURL="https://us-central1-cn2324-t1-g09.cloudfunctions.net/lookupFunction?zone=us-central1-c&prjid=cn2324-t1-g09&group=grpc-servers";
    private static int svcPort = 8000;
    private static ManagedChannel channel;
    private static ServiceSFGrpc.ServiceSFStub stubSF;

    private static ServiceSFGrpc.ServiceSFBlockingStub blockingStubSF;
    private static ServiceSGGrpc.ServiceSGBlockingStub blockingStubSG;

    private static List<String> fetchIPAddresses() {
        List<String> listips = new ArrayList<>();
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();

            HttpGet reqGet = new HttpGet(cfURL);
            CloseableHttpResponse respGet = httpclient.execute(reqGet);
            HttpEntity entity = respGet.getEntity();
            String jstr = EntityUtils.toString(entity);

            System.out.println("json string="+jstr);

            Type listType = new TypeToken<ArrayList<String>>(){}.getType();
            listips = new Gson().fromJson(jstr, listType);

            httpclient.close();
        } catch (Exception ex) {
            System.out.println("Error fetching IP addresses: " + ex.getMessage());
        }
        return listips;
    }

    static int Menu() {
        Scanner scan = new Scanner(System.in);
        int option;
        do {
            System.out.println("######## MENU ##########");
            System.out.println("Options for Google Storage Operations:");
            System.out.println(" 0: Operações funcionais (SF)");
            System.out.println(" 1: Operações para gestão de elasticidade (SG)");
            System.out.println("..........");
            System.out.println("3: Exit");
            System.out.print("Enter an Option: ");
            option = scan.nextInt();
        } while (!((option >= 0 && option <= 1) || option == 3));
        return option;
    }

    static int SubMenu(int mainOption) {
        Scanner scan = new Scanner(System.in);
        int option = 0;
        do {
            if (mainOption == 0) {
                System.out.println("######## MENU ##########");
                System.out.println("Options for Google Storage Operations:");
                System.out.println(" 0: Submit a new image to detect characteristics");
                System.out.println(" 1: Obtain characteristics and date of an image by its unique ID");
                System.out.println(" 2: Obtain names of files in the system between two dates");
                System.out.println("..........");
                System.out.println("3: Exit");
                System.out.print("Enter an Option: ");
                option = scan.nextInt();
            } else if (mainOption == 1) {
                System.out.println("######## MENU ##########");
                System.out.println("Options for Google Storage Operations:");
                System.out.println(" 0: Add or remove gRPC server instances");
                System.out.println(" 1: Add or remove application server instances");
                System.out.println("..........");
                System.out.println("3: Exit");
                System.out.print("Enter an Option: ");
                option = scan.nextInt();
            }
        } while (!((option >= 0 && option <= 2) || option == 3));
        return option;
    }

    public static void main(String[] args) throws Exception {
        boolean end = false;
        CountDownLatch operationLatch = new CountDownLatch(1);
        /*
        // Fetch IP addresses at the start
        List<String> ips = fetchIPAddresses();
        if (ips.isEmpty()) {
            System.out.println("No IP addresses found. Exiting...");
            return;
        }

        // Display IP addresses and let the user choose one
        Scanner scan = new Scanner(System.in);
        System.out.println("Available gRPC server IPs:");
        for (int i = 0; i < ips.size(); i++) {
            System.out.println(i + ": " + ips.get(i));
        }
        System.out.print("Choose an IP by entering its number: ");
        int ipChoice = scan.nextInt();
        String chosenIP = ips.get(ipChoice);*/
        Scanner scan = new Scanner(System.in);
        String chosenIP = "localhost";

        channel = ManagedChannelBuilder.forAddress(chosenIP, svcPort).usePlaintext().build();

        while (!end) {
            try {
                int option = Menu();
                switch (option) {
                    case 0:
                        int subOption0 = SubMenu(option);
                        switch (subOption0) {
                            case 0:
                                submitImage(operationLatch, scan);
                                break;
                            case 1:
                                // Obtain characteristics and date of an image by its unique ID
                                blockingStubSF = ServiceSFGrpc.newBlockingStub(channel);

                                System.out.print("Enter the unique ID of the image: ");
                                String uniqueId = scan.next();

                                // Create request object
                                ImageDetailsRequest imageDetailsRequest = ImageDetailsRequest.newBuilder()
                                        .setUniqueId(uniqueId)
                                        .build();

                                ImageDetailsResponse response = blockingStubSF.getImageDetails(imageDetailsRequest);

                                System.out.println("Image Details:");
                                System.out.println("Characteristics: " + response.getCharacteristicsList());
                                System.out.println("Translations: " + response.getTranslationsList());
                                System.out.println("Processed Date: " + response.getProcessedDate());
                                break;
                            case 2:
                                // Obtain names of files in the system between two dates
                                getNamesBetweenDates(scan);
                                break;
                            case 3:
                                end = true;
                                break;
                            default:
                                System.out.println("Invalid Option!");
                        }
                        break;
                    case 1:
                        blockingStubSG = ServiceSGGrpc.newBlockingStub(channel);
                        int subOption1 = SubMenu(option);
                        switch (subOption1) {
                            case 0:
                                System.out.print("Enter the number of gRPC server instances to scale to: ");
                                int numServerInstances = scan.nextInt();

                                // Send the scale request to the server
                                ScaleServerInstancesRequest serverGrpcInstancesRequest = ScaleServerInstancesRequest.newBuilder()
                                        .setNumInstances(numServerInstances)
                                        .build();
                                ScaleServerInstancesResponse serverGrpcInstancesResponse = blockingStubSG.scaleServerInstances(serverGrpcInstancesRequest);
                                System.out.println(serverGrpcInstancesResponse.getMessage());

                                break;
                            case 1:
                                System.out.print("Enter the number of Image Processing server instances to scale to: ");
                                int nummageProcessingInstances = scan.nextInt();

                                // Send the scale request to the server
                                ScaleImageProcessorRequest serverImageProcessingRequest = ScaleImageProcessorRequest.newBuilder()
                                        .setNumInstances(nummageProcessingInstances)
                                        .build();
                                ScaleImageProcessorResponse serverImageProcessingResponse = blockingStubSG.scaleImageProcessorsInstances(serverImageProcessingRequest);
                                System.out.println(serverImageProcessingResponse.getMessage());
                                break;
                            case 3:
                                end = true;
                                break;
                            default:
                                System.out.println("Invalid Option!");
                        }
                        break;
                    case 3:
                        end = true;
                }
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Error starting client app with exception: " + ex.getMessage());
            }
        }
    }

    // Function to handle image submission
    static private void submitImage(CountDownLatch operationLatch, Scanner scan) {
        stubSF = ServiceSFGrpc.newStub(channel);
        try {
            System.out.print("Enter the path to the image file: ");
            String imagePathString = scan.next();
            Path imagePath = Paths.get(imagePathString);
            byte[] imageBytes = Files.readAllBytes(imagePath);

            // Create a stream observer to handle the response from the server
            StreamObserver<ImageSubmissionResponse> responseObserver = createResponseObserver(operationLatch);

            // Send the image bytes to the server
            StreamObserver<ImageSubmissionRequest> requestObserver = stubSF.submitImage(responseObserver);
            requestObserver.onNext(ImageSubmissionRequest.newBuilder().setImageChunk(ByteString.copyFrom(imageBytes)).build());
            requestObserver.onCompleted();

            // Wait for the operation to complete
            operationLatch.await();
        } catch (IOException | InterruptedException e) {
            System.out.println("Error reading image file: " + e.getMessage());
        }
    }

    // Function to create a response observer
    static private StreamObserver<ImageSubmissionResponse> createResponseObserver(CountDownLatch operationLatch) {
        return new StreamObserver<>() {
            @Override
            public void onNext(ImageSubmissionResponse response) {
                System.out.println("Image submitted successfully. Unique ID: " + response.getUniqueId());
                operationLatch.countDown();
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("Error submitting image: " + t.getMessage());
                operationLatch.countDown();
            }

            @Override
            public void onCompleted() {
                // The server has completed processing the request
            }
        };
    }

    static private void getNamesBetweenDates(Scanner scan) {
        blockingStubSF = ServiceSFGrpc.newBlockingStub(channel);

        System.out.print("Enter the start date (dd-MM-yyyy): ");
        String startDate = scan.next();
        System.out.print("Enter the end date (dd-MM-yyyy): ");
        String endDate = scan.next();
        System.out.print("Enter the characteristic: ");
        String characteristic = scan.next();

        AllFilesWithRequest allFilesWithRequest = AllFilesWithRequest.newBuilder()
                .setStartDate(startDate)
                .setEndDate(endDate)
                .setCharacteristic(characteristic)
                .build();

        try {
            Iterator<AllFilesWithResponse> responseIterator = blockingStubSF.getAllFiles(allFilesWithRequest);

            // Process each response in the stream
            while (responseIterator.hasNext()) {
                AllFilesWithResponse res = responseIterator.next();
                // Print or process the file names from the response
                System.out.println("Received file names: " + res.getFileNamesList());
            }
        } catch (Exception e) {
            System.out.println("Error fetching file names: " + e.getMessage());
        }
    }
}