package google.firestore.models;

import java.util.List;

public class ImageInformation {
    private String requestId;
    private TranslationInformation translationInfo;
    private VisionInformation visionInfo;
    private String timestamp;

    public ImageInformation() {
    }

    public ImageInformation(String requestId, String timestamp, TranslationInformation translationInfo, VisionInformation visionInfo) {
        this.requestId = requestId;
        this.timestamp = timestamp;
        this.translationInfo = translationInfo;
        this.visionInfo = visionInfo;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public List<String> getTranslationInfo() {
        return translationInfo.getDetails();
    }

    public List<String> getVisionInfo() {
        return visionInfo.getDetails();
    }
}

