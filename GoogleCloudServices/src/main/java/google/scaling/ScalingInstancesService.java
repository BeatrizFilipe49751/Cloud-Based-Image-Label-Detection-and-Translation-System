package google.scaling;

import com.google.api.gax.longrunning.OperationFuture;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import com.google.cloud.compute.v1.*;

public class ScalingInstancesService {
    private final String project = "CN2324-T1-G09";
    private final String zone = "us-central1-c";
    private final String instanceGroupName = "grpc-servers";

    public ScalingInstancesService() {
    }

    public boolean scaleServerInstancesLogic(int numInstances) {
        try {
            InstanceGroupManagersClient managersClient = InstanceGroupManagersClient.create();

            OperationFuture<Operation, Operation> result = managersClient.resizeAsync(
                    project,
                    zone,
                    instanceGroupName,
                    numInstances
            );

            // Wait for the operation to complete and get the result
            Operation operation = result.get();

            if (operation != null) {
                return true; // Operation completed successfully
            } else {
                System.out.println("Instance scaling failed");
                return false; // Operation failed
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            System.out.println("Instance scaling failed: " + e.getMessage());
            return false;
        }
    }

}
