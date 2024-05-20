package google.firestore.models;

import com.google.cloud.firestore.annotation.DocumentId;

public class LogEntry {
    @DocumentId
    private String id;
    private final String requestId;
    private final String timestamp;
    private final String details;

    public LogEntry(String requestId, String timestamp, String details) {
        this.requestId = requestId;
        this.timestamp = timestamp;
        this.details = details;
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

    public String getDetails() {
        return details;
    }
}
