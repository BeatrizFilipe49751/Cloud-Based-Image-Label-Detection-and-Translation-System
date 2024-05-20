package google.firestore.models;

public record LogEntry(String requestId, String timestamp, String details) {
}
