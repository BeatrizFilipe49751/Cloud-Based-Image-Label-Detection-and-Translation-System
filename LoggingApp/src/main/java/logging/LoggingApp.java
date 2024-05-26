package logging;

import com.google.cloud.Timestamp;
import com.google.protobuf.ByteString;
import google.firestore.FirestoreService;
import google.firestore.models.LogEntry;
import google.pubsub.PubSubService;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingApp {
    private static final Logger logger = Logger.getLogger(LoggingApp.class.getName());
    private final FirestoreService firestoreService;
    private final PubSubService pubSubService;
    private final Schema schema;

    public LoggingApp() {
        try {
            this.firestoreService = new FirestoreService();
            this.pubSubService = new PubSubService();
            this.schema = new Schema.Parser().parse(getClass().getResourceAsStream("/logging/messageSchema.avsc"));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read schema file.");
        }
    }

    public void checkSub() {
        pubSubService.subscribeMessageLogging((message, consumer) -> {
            ByteString data = message.getData();
            try {
                GenericRecord record = deserializeAvroSchema(data);

                Map<String, String> attributes = message.getAttributesMap();
                String timestamp = attributes.get("timestamp");

                Timestamp firestoreTimestamp = Timestamp.parseTimestamp(timestamp);

                firestoreService.saveLog(new LogEntry(record.get("id").toString(), firestoreTimestamp));
                consumer.ack();
            } catch (IOException | ExecutionException | InterruptedException e) {
                consumer.nack();
                logger.log(Level.WARNING, e.getMessage());
            }
        });
    }

    private GenericRecord deserializeAvroSchema(ByteString data) throws IOException {
        String dataString = data.toStringUtf8();
        DatumReader<GenericRecord> reader = new GenericDatumReader<>(schema);
        Decoder decoder = DecoderFactory.get().jsonDecoder(schema, dataString);
        return reader.read(null, decoder);
    }
}
