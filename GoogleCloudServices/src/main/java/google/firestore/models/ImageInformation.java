package google.firestore.models;

import com.google.cloud.firestore.annotation.DocumentId;

public record ImageInformation(String requestId, String timestamp, TranslationInformation translationInfo, VisionInformation visionInfo) {
    @DocumentId
    private static String id;

}

