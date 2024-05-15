package google.firestore;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import google.firestore.models.LogEntry;

import java.util.concurrent.ExecutionException;

public class FirestoreService {

    private final Firestore db;
    private final CollectionReference logsCollection;

    public FirestoreService(String dbName) {
        this.db = FirestoreOptions
                .newBuilder().setDatabaseId(dbName).setCredentials(/*Faltam as credenciais*/)
                .build().getService();
        this.logsCollection = db.collection("Logs");
    }

    public void saveLog(LogEntry logEntry) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> future = logsCollection.add(logEntry);
        future.get();
    }



}
