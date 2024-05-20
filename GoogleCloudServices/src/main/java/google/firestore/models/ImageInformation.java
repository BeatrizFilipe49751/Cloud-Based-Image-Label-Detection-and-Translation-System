package google.firestore.models;

import com.google.cloud.firestore.annotation.DocumentId;

public class ImageInformation {
    @DocumentId
    private String id;

    public ImageInformation(String requestId, String timestamp, TranslationInformation translationInfo, VisionInformation visionInfo) {
    }
}

