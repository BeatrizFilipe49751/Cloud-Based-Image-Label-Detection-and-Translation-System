package google.cloudstorage;

import com.google.cloud.WriteChannel;
import com.google.cloud.storage.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

public class CloudStorageService {
    private final Storage storage;
    private final String bucketName = "cn-tp-g09-bucket";


    public CloudStorageService() {
        storage = StorageOptions.getDefaultInstance().getService();
    }

    public String storeImageBytes(byte[] imageData, String blobName) {
        BlobId blobId = BlobId.of(bucketName, blobName);
        Blob blob = storage.get(blobId);
        if (blob == null) {
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            blob = storage.create(blobInfo);
        }
        byte[] existingBytes = blob.getContent();
        byte[] newBytes = concatenateBytes(existingBytes, imageData);
        try (WriteChannel writer = blob.writer()) {
            writer.write(ByteBuffer.wrap(newBytes));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return blob.getMediaLink();
    }

    public String generateUniqueBlobName() {
        return bucketName + "-image-" + UUID.randomUUID();
    }

    private byte[] concatenateBytes(byte[] existingBytes, byte[] newBytes) {
        byte[] result = new byte[existingBytes.length + newBytes.length];
        System.arraycopy(existingBytes, 0, result, 0, existingBytes.length);
        System.arraycopy(newBytes, 0, result, existingBytes.length, newBytes.length);
        return result;
    }

}
