package google.firestore;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import google.firestore.models.ImageInformation;
import google.firestore.models.LogEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;


// CRIAR UM SERVIÇO FIRESTORE DIFERENTE PARA AS IMAGENS E AS MENSAGENS
public class FirestoreService {

    private final CollectionReference logsCollection;
    private final CollectionReference characteristicsCollection;

    public FirestoreService() {
        Firestore db = FirestoreOptions
                .newBuilder().setDatabaseId("db_name").setCredentials(/*Faltam as credenciais*/)
                .build().getService();
        this.logsCollection = db.collection("Logs");
        this.characteristicsCollection = db.collection("ImagesDetails");
    }

    public void saveLog(LogEntry logEntry) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> future = logsCollection.add(logEntry);
        future.get();
    }

    public void saveImageInfo(ImageInformation imageInfo) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> future = characteristicsCollection.add(imageInfo);
        future.get();
    }

    public ImageInformation getImageInfo(String uniqueID) throws ExecutionException, InterruptedException {
        DocumentReference docRefRead = characteristicsCollection.document(uniqueID);
        ApiFuture<DocumentSnapshot> future = docRefRead.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return document.toObject(ImageInformation.class);
        } else {
            return null;
        }
    }

    public List<ImageInformation> getImageInfoBetweenCertainDatesAndWith(String startDate, String endDate, String characteristic) throws ParseException, ExecutionException, InterruptedException {
        List<ImageInformation> images = new java.util.ArrayList<>(List.of());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Timestamp start = Timestamp.of(formatter.parse(startDate));
        Timestamp end = Timestamp.of(formatter.parse(endDate));
        Query query = characteristicsCollection
                .whereGreaterThan("timestamp", start)
                .whereLessThan("timestamp", end)
                .whereArrayContains("visionInformation.details", characteristic);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot doc: querySnapshot.get().getDocuments()) {
            images.add(doc.toObject(ImageInformation.class));
        }
        return images;
    }

}
