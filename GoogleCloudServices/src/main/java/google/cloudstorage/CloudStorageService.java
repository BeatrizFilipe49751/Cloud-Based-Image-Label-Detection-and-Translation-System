package google.cloudstorage;

import com.google.cloud.storage.*;

import java.util.UUID;

public class CloudStorageService {
    private final Storage storage;
    private final String bucketName = "bucketName";

    public CloudStorageService() {
        storage = StorageOptions.getDefaultInstance().getService();
    }

    public String storeImage(String imageData) {
        String blobName = generateUniqueBlobName();

        BlobId blobId = BlobId.of(bucketName, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        Blob blob = storage.create(blobInfo, imageData.getBytes());

        return blob.getBucket() + "/" + blob.getName();
    }

    public String generateUniqueBlobName() {
        return bucketName + "-image-" + UUID.randomUUID();
    }

}
