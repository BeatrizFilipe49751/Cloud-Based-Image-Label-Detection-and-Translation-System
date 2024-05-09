package serviceimpl.servicesf;

import io.grpc.stub.StreamObserver;
import servicesf.*;

public class ServiceSF extends ServiceSFGrpc.ServiceSFImplBase {
    public ServiceSF(int port) {}

    @Override
    public void submitImage(ImageSubmissionRequest request, StreamObserver<ImageSubmissionResponse> responseObserver) {}

    @Override
    public void getImageDetails(ImageDetailsRequest request, StreamObserver<ImageDetailsResponse> responseObserver) {}

    @Override
    public void getAllFiles(AllFilesWithRequest request, StreamObserver<AllFilesWithResponse> responseObserver) {}
}
