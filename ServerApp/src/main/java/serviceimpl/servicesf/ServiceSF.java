package serviceimpl.servicesf;

import google.cloudstorage.CloudStorageService;
import io.grpc.stub.StreamObserver;
import servicesf.*;

public class ServiceSF extends ServiceSFGrpc.ServiceSFImplBase {
    public ServiceSF(int port) {
    }

    private final CloudStorageService cs = new CloudStorageService();

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
                responseObserver.onNext(ImageSubmissionResponse.newBuilder().setUniqueId(blobLink).build());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void getImageDetails(ImageDetailsRequest request, StreamObserver<ImageDetailsResponse> responseObserver) {

    }

    @Override
    public void getAllFiles(AllFilesWithRequest request, StreamObserver<AllFilesWithResponse> responseObserver) {
    }
}
