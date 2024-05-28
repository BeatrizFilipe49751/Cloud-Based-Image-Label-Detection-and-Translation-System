package google.scaling;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.compute.v1.InstanceGroupManagersClient;
import com.google.cloud.compute.v1.Operation;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScalingInstancesService {
    private final Logger logger = Logger.getLogger(ScalingInstancesService.class.getName());
    private final String project = "cn2324-t1-g09";
    private final String zone = "us-central1-c";
    private final String ServerInstancesGroupName = "grpc-servers";
    private final String ImageProcessingInstancesGroupName = "labels-app-workers";

    public ScalingInstancesService() {
    }

    public boolean scaleServerInstancesLogic(int numInstances) {
        return scaleInstances(ServerInstancesGroupName, numInstances);
    }

    public boolean scaleImageProcessingInstancesLogic(int numInstances) {
        return scaleInstances(ImageProcessingInstancesGroupName, numInstances);
    }

    private boolean scaleInstances(String instanceGroupName, int numInstances){
        try (InstanceGroupManagersClient managersClient = InstanceGroupManagersClient.create()) {
            OperationFuture<Operation, Operation> result = managersClient.resizeAsync(project, zone, instanceGroupName, numInstances);

            // Wait for the operation to complete and get the result
            Operation operation = result.get();
            if (operation != null) {
                logger.log(Level.INFO, "Successfully resized instances to " + numInstances);
                return true; // Operation completed successfully
            } else {
                logger.log(Level.WARNING, "Instance scaling failed");
                return false; // Operation failed
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            logger.log(Level.WARNING, "Instance scaling failed with exception: " + e.getMessage());
            return false;
        }
    }
}

