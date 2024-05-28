package serverapp;

import io.grpc.ServerBuilder;
import serviceimpl.servicesf.ServiceSF;
import serviceimpl.servicesg.ServiceSG;
import shutdownhook.ShutdownHook;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerApp{
    private static final Logger logger = Logger.getLogger(ServerApp.class.getName());
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
            logger.log(Level.INFO, "Server started on port " + svcPort);
            // Java virtual machine shutdown hook
            // to capture normal or abnormal exits
            Runtime.getRuntime().addShutdownHook(new ShutdownHook(svc));
            // Waits for the server to become terminated
            svc.awaitTermination();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error starting server with exception: ", ex);
        }
    }
}
