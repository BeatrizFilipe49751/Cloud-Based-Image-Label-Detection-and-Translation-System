package serviceimpl.servicesf;

import google.cloudstorage.CloudStorageService;
import io.grpc.stub.StreamObserver;
import servicesf.*;

public class ServiceSF extends ServiceSFGrpc.ServiceSFImplBase {
    public ServiceSF(int port) {}

    private final CloudStorageService cs = new CloudStorageService();

    @Override
    public StreamObserver<ImageSubmissionRequest> submitImage(ImageSubmissionRequest request, StreamObserver<ImageSubmissionResponse> responseObserver) {
        return new StreamObserver<>() {
            private final StringBuilder imageData = new StringBuilder();

            @Override
            public void onNext(ImageSubmissionRequest imageSubmissionRequest) {
                imageData.append(new String(request.getImageChunk().toByteArray()));
            }

            @Override
            public void onError(Throwable t) {
                // FALTA TRATAR ERROS
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                // Ao concluir o streaming, armazena a imagem no Cloud Storage e retorna o ID do pedido
                String requestId = cs.storeImage(imageData.toString());
                responseObserver.onNext(ImageSubmissionResponse.newBuilder().setUniqueId(requestId).build());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void getImageDetails(ImageDetailsRequest request, StreamObserver<ImageDetailsResponse> responseObserver) {

    }

    @Override
    public void getAllFiles(AllFilesWithRequest request, StreamObserver<AllFilesWithResponse> responseObserver) {}
}
