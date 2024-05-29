package labels;

import com.google.cloud.Timestamp;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import google.firestore.*;
import google.firestore.models.ImageInformation;
import google.firestore.models.TranslationInformation;
import google.firestore.models.VisionInformation;
import google.pubsub.PubSubService;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class LabelsApp {
    private final FirestoreService firestoreService;
    private final PubSubService pubSubService;
    private final Schema schema;

    public LabelsApp() {
        try {
            this.firestoreService = new FirestoreService();
            this.pubSubService = new PubSubService();
            this.schema = new Schema.Parser().parse(getClass().getResourceAsStream("/labels/messageSchema.avsc"));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read schema file.");
        }
    }

    public void checkSub() {
        pubSubService.subscribeMessageLabels((message, consumer) -> {
            ByteString data = message.getData();
            try {
                GenericRecord record = deserializeAvroSchema(data);
                String id = record.get("id").toString();
                String bucketName = record.get("bucketName").toString();
                String blobName = record.get("blobName").toString();
                Map<String, String> attributes = message.getAttributesMap();
                String timestamp = attributes.get("timestamp");

                Timestamp firestoreTimestamp = Timestamp.parseTimestamp(timestamp);

                List<String> labels = detectLabels(bucketName, blobName);
                List<String> labelsTranslated = TranslateLabels(labels);
                TranslationInformation translationInformation = new TranslationInformation(labelsTranslated);
                VisionInformation visionInformation = new VisionInformation(labels);
                ImageInformation imageInformation = new ImageInformation(id, firestoreTimestamp, translationInformation, visionInformation);
                firestoreService.saveImageInfo(imageInformation);
                consumer.ack();
            } catch (IOException | ExecutionException | InterruptedException e) {
                consumer.nack();
                System.out.println("Error: " + e.getMessage());
            }
        });
    }

    public List<String> detectLabels(String bucketName, String blobName) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        String gsURI = String.format("gs://%s/%s", bucketName, blobName);

        Image img = Image.newBuilder()
                .setSource(ImageSource.newBuilder().setImageUri(gsURI).build())
                .build();

        Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();

        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();
            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                } else {
                    for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                        labels.add(annotation.getDescription());
                    }
                }
            }
        }
        return labels;
    }

    public List<String> TranslateLabels(List<java.lang.String> labels) {
        List<java.lang.String> labelsTranslated = null;
        try {
            Translate translateService = TranslateOptions.getDefaultInstance().getService();
            labelsTranslated = new ArrayList<>();
            for (java.lang.String label : labels) {
                Translation translation = translateService.translate(
                        label,
                        Translate.TranslateOption.sourceLanguage("en"),
                        Translate.TranslateOption.targetLanguage("pt"));
                labelsTranslated.add(translation.getTranslatedText());
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        } finally {
            return labelsTranslated;
        }
    }

    private GenericRecord deserializeAvroSchema(ByteString data) throws IOException {
        String dataString = data.toStringUtf8();
        DatumReader<GenericRecord> reader = new GenericDatumReader<>(schema);
        Decoder decoder = DecoderFactory.get().jsonDecoder(schema, dataString);
        return reader.read(null, decoder);
    }

    public static void main(String[] args) {
        LabelsApp app = new LabelsApp();
        app.checkSub();
        // Keep the application running to listen for messages
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}