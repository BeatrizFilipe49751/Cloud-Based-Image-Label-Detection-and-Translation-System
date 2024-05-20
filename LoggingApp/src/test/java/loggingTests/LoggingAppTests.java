package loggingTests;

import com.google.pubsub.v1.PubsubMessage;
import com.google.protobuf.ByteString;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import google.firestore.FirestoreService;
import google.pubsub.PubSubService;
import logging.LoggingApp;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.io.DatumWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoggingAppTest {

    @Mock
    private FirestoreService firestoreService;

    @Mock
    private PubSubService pubSubService;

    @InjectMocks
    private LoggingApp loggingApp;

    private Schema schema;

    @BeforeEach
    public void setUp() throws IOException {
        schema = new Schema.Parser().parse(getClass().getResourceAsStream("/avro_schema.avsc"));
        loggingApp = new LoggingApp("test-project", "test-subscription");
        loggingApp = spy(loggingApp);
        doReturn(firestoreService).when(loggingApp).getFirestoreService();
        doReturn(pubSubService).when(loggingApp).getPubSubService();
    }

    @Test
    public void testDeserializeAvro() throws IOException {
        // Create a test Avro record
        GenericRecord record = new GenericData.Record(schema);
        record.put("id", 123);
        record.put("bucketName", "test-bucket");
        record.put("blobName", "test-blob");

        // Serialize the record to a byte array
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DatumWriter<GenericRecord> writer = new SpecificDatumWriter<>(schema);
        Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
        writer.write(record, encoder);
        encoder.flush();
        out.close();

        byte[] data = out.toByteArray();

        // Deserialize the record
        GenericRecord deserializedRecord = loggingApp.deserializeAvro(data);

        // Assert the deserialized values
        assertEquals(123, deserializedRecord.get("id"));
        assertEquals("test-bucket", deserializedRecord.get("bucketName"));
        assertEquals("test-blob", deserializedRecord.get("blobName"));
    }

    @Test
    public void testReceiveMessage() throws IOException, ExecutionException, InterruptedException {
        // Create a test Avro record
        GenericRecord record = new GenericData.Record(schema);
        record.put("id", 123);
        record.put("bucketName", "test-bucket");
        record.put("blobName", "test-blob");

        // Serialize the record to a byte array
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DatumWriter<GenericRecord> writer = new SpecificDatumWriter<>(schema);
        Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
        writer.write(record, encoder);
        encoder.flush();
        out.close();

        byte[] data = out.toByteArray();

        // Create a test PubsubMessage
        PubsubMessage message = PubsubMessage.newBuilder().setData(ByteString.copyFrom(data)).build();
        AckReplyConsumer consumer = mock(AckReplyConsumer.class);

        // Call the receiveMessage method
        MessageReceiver receiver = loggingApp.createMessageReceiver();
        receiver.receiveMessage(message, consumer);

        // Verify that the message was logged correctly
        Map<String, Object> expectedLogData = new HashMap<>();
        expectedLogData.put("id", 123);
        expectedLogData.put("bucketName", "test-bucket");
        expectedLogData.put("blobName", "test-blob");
        expectedLogData.put("timestamp", anyLong());

        verify(firestoreService, times(1)).logMessage(eq("Logs"), argThat(new LogDataMatcher(expectedLogData)));
        verify(consumer, times(1)).ack();
    }

    private record LogDataMatcher(Map<String, Object> expected) implements ArgumentMatcher<Map<String, Object>> {

        @Override
            public boolean matches(Map<String, Object> argument) {
                if (argument == null) {
                    return false;
                }
                for (Map.Entry<String, Object> entry : expected.entrySet()) {
                    if (!argument.containsKey(entry.getKey())) {
                        return false;
                    }
                    if (!entry.getValue().equals(argument.get(entry.getKey()))) {
                        return false;
                    }
                }
                return true;
            }
        }
}
