package google.firestore.models;

import com.google.cloud.firestore.annotation.DocumentId;

public class ImageInformation {
    @DocumentId
    private String id;
    private final String requestId;
    private final String timestamp;
    private final TranslationInformation translationInfo;
    private final VisionInformation visionInfo;

    public ImageInformation(String requestId, String timestamp, TranslationInformation translationInfo, VisionInformation visionInfo) {
        this.requestId = requestId;
        this.timestamp = timestamp;
        this.translationInfo = translationInfo;
        this.visionInfo = visionInfo;
    }

    public String getId() {
        return id;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public TranslationInformation getTranslationInfo() {
        return translationInfo;
    }
    public VisionInformation getVisionInfo() {
        return visionInfo;
    }
}

