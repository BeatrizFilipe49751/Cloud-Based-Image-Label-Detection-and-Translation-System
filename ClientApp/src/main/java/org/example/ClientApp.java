package org.example;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientApp {

    // To change - cloud function URL
    static String cfURL="https://us-central1-cn2324-t1-g09.cloudfunctions.net/lookupFunction?zone=us-central1-a&prjid=cn2324-t1-g09&group=grpc-servers";

    private static List<String> fetchIPAddresses() throws Exception {
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
            ex.printStackTrace();
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
                System.out.println(" 1: Obtain names of files in the system between two dates");
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
        } while (!((option >= 0 && option <= 1) || option == 3));
        return option;
    }

    public static void main(String[] args) throws Exception {
        boolean end = false;
        while (!end) {
            try {
                int option = Menu();
                switch (option) {
                    case 0:
                        int subOption0 = SubMenu(option);
                        switch (subOption0) {
                            case 0:
                                // Submit a new image to detect characteristics
                                break;
                            case 1:
                                // Obtain names of files in the system between two dates
                                break;
                            case 3:
                                end = true;
                                break;
                            default:
                                System.out.println("Invalid Option!");
                        }
                        break;
                    case 1:
                        int subOption1 = SubMenu(option);
                        switch (subOption1) {
                            case 0:
                                // Add or remove gRPC server instances
                                break;
                            case 1:
                                // Add or remove application server instances
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
                System.out.println("Error executing operations!");
                ex.printStackTrace();
            }
        }
    }

}