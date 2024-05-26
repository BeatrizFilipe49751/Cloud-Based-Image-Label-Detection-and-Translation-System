package serviceimpl.servicesf;

import google.cloudstorage.CloudStorageService;
import google.firestore.FirestoreService;
import google.firestore.models.ImageInformation;
import google.pubsub.PubSubService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import labels.LabelsApp;
import logging.LoggingApp;
import servicesf.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;

public class ServiceSF extends ServiceSFGrpc.ServiceSFImplBase {
    public ServiceSF(int port) {
        loggingApp.checkSub();
        labelsApp.checkSub();
    }

    private final CloudStorageService cs = new CloudStorageService();
    private final FirestoreService fs = new FirestoreService();

    private final PubSubService pubSubService = new PubSubService();

    private final LoggingApp loggingApp = new LoggingApp("CN2324-T1-G09", "LoggingAppSub");

    private final LabelsApp labelsApp = new LabelsApp();



    @Override
    public StreamObserver<ImageSubmissionRequest> submitImage(StreamObserver<ImageSubmissionResponse> responseObserver) {
        return new StreamObserver<>() {
            private final String uniqueBlobId = cs.generateUniqueBlobName();
            private String blobLink = "";

            @Override
            public void onNext(ImageSubmissionRequest imageSubmissionRequest) {
                blobLink = cs.storeImageBytes(imageSubmissionRequest.getImageChunk().toByteArray(), uniqueBlobId);
            }

            @Override
            public void onError(Throwable t) {
                // FALTA TRATAR ERROS
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                try {
                    pubSubService.publishMessage(blobLink, "cn-tp-g09-bucket", uniqueBlobId);
                } catch (IOException | InterruptedException e) {
                    System.out.println("Error publishing message to Pub/Sub: " + e.getMessage());
                }
                responseObserver.onNext(ImageSubmissionResponse.newBuilder().setUniqueId(blobLink).build());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void getImageDetails(ImageDetailsRequest request, StreamObserver<ImageDetailsResponse> responseObserver) {

        try {
            ImageInformation imageInfo = fs.getImageInfo(request.getUniqueId());

            ImageDetailsResponse.Builder responseBuilder = ImageDetailsResponse.newBuilder();

            // Add characteristics
            responseBuilder.addAllCharacteristics(imageInfo.getVisionInfo());

            // Add translations
            responseBuilder.addAllTranslations(imageInfo.getTranslationInfo());

            // Add processed date (if available)
            if (imageInfo.getTimestamp() != null) {
                responseBuilder.setProcessedDate(imageInfo.getTimestamp());
            }

            // Build the response
            ImageDetailsResponse response = responseBuilder.build();

            // Send the response back to the client
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            // e.message
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getAllFiles(AllFilesWithRequest request, StreamObserver<AllFilesWithResponse> responseObserver) {
        // TODO: Implement getAllFiles
        int count = 0;
        try {
            for (String imageInfo : fs.getImageFileNameBetweenCertainDatesAndWith(request.getStartDate(), request.getEndDate(), request.getCharacteristic())){
                responseObserver.onNext(AllFilesWithResponse.newBuilder()
                        .setFileNames(count, imageInfo)
                        .build());
                count++;
            }
        } catch (ExecutionException | InterruptedException | ParseException e) {
            System.out.println("Error getting all files: " + e.getMessage());
        }
    }
}
