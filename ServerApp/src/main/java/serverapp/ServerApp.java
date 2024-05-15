package serverapp;

import io.grpc.ServerBuilder;
import serviceimpl.servicesf.ServiceSF;
import serviceimpl.servicesg.ServiceSG;
import shutdownhook.ShutdownHook;

public class ServerApp{
    private static int svcPort = 8000;

    public static void main(String[] args) {
        try {
            if (args.length > 0) svcPort = Integer.parseInt(args[0]);
            io.grpc.Server svc = ServerBuilder.forPort(svcPort)
                    // Add one or more services.
                    // The Server can host many services in same TCP/IP port
                    .addService(new ServiceSF(svcPort))
                    .addService(new ServiceSG(svcPort))
                    .build();
            svc.start();
            System.out.println("Server started on port " + svcPort);
            // Java virtual machine shutdown hook
            // to capture normal or abnormal exits
            Runtime.getRuntime().addShutdownHook(new ShutdownHook(svc));
            // Waits for the server to become terminated
            svc.awaitTermination();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
