package google.firestore.models;

public class LogEntry {
    private String requestId;
    private String timestamp;

    public LogEntry() {}

    public LogEntry(String requestId, String timestamp) {
        this.requestId = requestId;
        this.timestamp = timestamp;
    }

    // Getter and setter methods
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
