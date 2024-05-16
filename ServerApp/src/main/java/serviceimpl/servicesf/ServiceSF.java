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
        return new StreamObserver<ImageSubmissionRequest>() {
            private final StringBuilder imageData = new StringBuilder();
            private final String uniqueId = cs.generateUniqueBlobName();

            @Override
            public void onNext(ImageSubmissionRequest imageSubmissionRequest) {
                String requestId = cs.storeImage(imageData.toString(), uniqueId); //TODO: Implementar o método storeImage
                imageData.append(new String(imageSubmissionRequest.getImageChunk().toByteArray()));
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
    public void getAllFiles(AllFilesWithRequest request, StreamObserver<AllFilesWithResponse> responseObserver) {
    }
}
