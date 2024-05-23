package serviceimpl.servicesf;

import google.cloudstorage.CloudStorageService;
import google.firestore.FirestoreService;
import google.firestore.models.ImageInformation;
import io.grpc.stub.StreamObserver;
import servicesf.*;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;

public class ServiceSF extends ServiceSFGrpc.ServiceSFImplBase {
    public ServiceSF(int port) {
    }

    private final CloudStorageService cs = new CloudStorageService();
    private final FirestoreService fs = new FirestoreService();

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
    public void getImageDetails(ImageDetailsRequest request, StreamObserver<ImageDetailsResponse> responseObserver) throws ExecutionException, InterruptedException {
        ImageInformation imageInfo = fs.getImageInfo(request.getUniqueId());
        if (imageInfo != null) {
            int count = 0;
            for (; ; ) {
                responseObserver.onNext(ImageDetailsResponse.newBuilder()
                        .setCharacteristics(count, imageInfo.visionInfo().details().get(count))
                        .setTranslations(count, imageInfo.translationInfo().details().get(count))
                        .build());
                count++;
                if (imageInfo.visionInfo().details().size() - 1 == count) break;
            }
            responseObserver.onNext(ImageDetailsResponse.newBuilder()
                    .setProcessedDate(imageInfo.timestamp())
                    .build());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new Throwable("Image not found"));
        }

    }

    @Override
    public void getAllFiles(AllFilesWithRequest request, StreamObserver<AllFilesWithResponse> responseObserver) throws ParseException, ExecutionException, InterruptedException {
        int count = 0;
        for (String imageInfo : fs.getImageFileNameBetweenCertainDatesAndWith(request.getStartDate(), request.getEndDate(), request.getCharacteristic())){
            responseObserver.onNext(AllFilesWithResponse.newBuilder()
                    .setFileNames(count, imageInfo)
                    .build());
            count++;
        }
    }
}