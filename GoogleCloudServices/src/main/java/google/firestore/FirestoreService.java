package google.firestore;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import google.firestore.models.ImageInformation;
import google.firestore.models.LogEntry;
import google.firestore.models.TranslationInformation;
import google.firestore.models.VisionInformation;

import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;


// CRIAR UM SERVIÇO FIRESTORE DIFERENTE PARA AS IMAGENS E AS MENSAGENS
public class FirestoreService {

    private final CollectionReference logsCollection;
    private final CollectionReference characteristicsCollection;

    public FirestoreService() {
        String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        if (credentialsPath == null) {
            throw new IllegalStateException("Environment variable GOOGLE_APPLICATION_CREDENTIALS not set.");
        }

        GoogleCredentials credentials;
        try (FileInputStream serviceAccount = new FileInputStream(credentialsPath)) {
            credentials = GoogleCredentials.fromStream(serviceAccount);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to read credentials file.");
        }

        Firestore db = FirestoreOptions
                .newBuilder().setDatabaseId("db-name").setCredentials(credentials)
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
        Query query = characteristicsCollection.whereEqualTo("requestId", uniqueID);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        DocumentSnapshot document = querySnapshot.get().getDocuments().get(0);
        if (document != null && document.exists()) {
            return fromDocumentSnapshot(document);
        } else {
            return null;
        }
    }

    public List<String> getImageFileNameBetweenCertainDatesAndWith(String startDate, String endDate, String characteristic) throws ParseException, ExecutionException, InterruptedException {
        List<String> images = new java.util.ArrayList<>(List.of());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        Date startParsed = formatter.parse(startDate);
        Date endParsed = formatter.parse(endDate);

        Timestamp start = Timestamp.of(startParsed);
        Timestamp end = Timestamp.of(endParsed);

        Query query = characteristicsCollection
                .whereGreaterThan("timestamp", start)
                .whereLessThan("timestamp", end)
                .whereArrayContains("visionInfo", characteristic);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot doc: querySnapshot.get().getDocuments()) {
            images.add(doc.getString("requestId"));
        }
        return images;
    }

    private static ImageInformation fromDocumentSnapshot(DocumentSnapshot document) {
        if (document != null && document.exists()) {
                String requestId = document.getString("requestId");
                Timestamp timestamp = document.getTimestamp("timestamp");
                List<String> translationInfoList = (List<String>) document.get("translationInfo");
                TranslationInformation translationInfo = new TranslationInformation(translationInfoList);
                List<String> visionInfoList = (List<String>) document.get("visionInfo");
                VisionInformation visionInfo = new VisionInformation(visionInfoList);
                return new ImageInformation(requestId, timestamp, translationInfo, visionInfo);
        } else {
            return null;
        }
    }

}