package logging;

import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.protobuf.ByteString;
import google.firestore.FirestoreService;
import google.firestore.models.LogEntry;
import google.pubsub.PubSubService;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class LoggingApp {
    private final FirestoreService firestoreService;
    private final PubSubService pubSubService;
    private final Schema schema;

    public LoggingApp(String projectId, String subscriptionId) throws IOException {
        this.firestoreService = new FirestoreService();
        this.pubSubService = new PubSubService();
        this.schema = new Schema.Parser().parse(getClass().getResourceAsStream("/avro_schema.avsc"));
    }

    public void checkSub() {
        pubSubService.subscribeMessageLogging((message, consumer) -> {
            ByteString data = message.getData();
            try {
                GenericRecord record = deserializeAvroSchema(data.toByteArray());
                firestoreService.saveLog(new LogEntry(record.get("id").toString(), record.get("bucketName").toString(), record.get("blobName").toString()));
                consumer.ack();
            } catch (IOException | ExecutionException | InterruptedException e) {
                consumer.nack();
                e.printStackTrace();
            }
        });
    }

    private GenericRecord deserializeAvroSchema(byte[] data) throws IOException {
        SpecificDatumReader<GenericRecord> reader = new SpecificDatumReader<>(schema);
        Decoder decoder = DecoderFactory.get().binaryDecoder(new ByteArrayInputStream(data), null);
        return reader.read(null, decoder);
    }
}
