package serviceimpl.servicesg;

import io.grpc.stub.StreamObserver;
import servicesg.*;

public class ServiceSG extends ServiceSGGrpc.ServiceSGImplBase {
    public ServiceSG(int port) {}

    @Override
    public void scaleServerInstances(ScaleServerInstancesRequest request, StreamObserver<ScaleServerInstancesResponse> responseObserver) {}

    @Override
    public void scaleImageProcessorsInstances(ScaleImageProcessorRequest request, StreamObserver<ScaleImageProcessorResponse> responseObserver) {}
}
