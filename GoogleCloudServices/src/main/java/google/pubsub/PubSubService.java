package google.pubsub;

// PubSubService.java

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.core.ApiService.Listener;
import com.google.api.core.ApiService.State;
import com.google.api.gax.core.ExecutorProvider;
import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PubSubService {
    private static final Logger logger = Logger.getLogger(PubSubService.class.getName());
    private final String projectId = "CN2324-T1-G09";
    private final String topicId = "GRPCServerMessages";
    private final String loggingAppSubscriptionID = "LoggingAppSub";
    private final String labelsAppSubscriptionID = "LabelsAppSub";
    private final ExecutorProvider executorProvider = InstantiatingExecutorProvider.newBuilder().setExecutorThreadCount(4).build();
    private Schema schema;

    public PubSubService() {
        try {
            // Create Avro schema
            this.schema = new Schema.Parser().parse(getClass().getResourceAsStream("/google/pubsub/messageSchema.avsc"));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading Avro schema: " + e.getMessage());
        }
    }

    public void publishMessage(String id, String bucketName, String blobName) throws IOException, InterruptedException {
        ProjectTopicName topicName = ProjectTopicName.of(projectId, topicId);
        Publisher publisher = null;

        try {
            publisher = Publisher.newBuilder(topicName).build();

            // Create Avro record
            GenericRecord record = new GenericData.Record(schema);
            record.put("id", id);
            record.put("bucketName", bucketName);
            record.put("blobName", blobName);

            ByteString data = ByteString.copyFromUtf8(record.toString());

            // Create Pub/Sub message
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                    .setData(data)
                    .putAttributes("timestamp", String.valueOf(System.currentTimeMillis()))
                    .build();

            // Publish message
            ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
            ApiFutures.addCallback(messageIdFuture, new ApiFutureCallback<String>() {
                @Override
                public void onFailure(Throwable t) {
                    if (t instanceof ApiException apiException) {
                        logger.log(Level.SEVERE, "Error publishing message: " + apiException.getStatusCode().getCode());
                        logger.log(Level.SEVERE, apiException.getMessage());
                    }
                }

                @Override
                public void onSuccess(String messageId) {
                    logger.log(Level.INFO, "Published message ID: " + messageId);
                }
            });
        } finally {
            if (publisher != null) {
                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
            }
        }
    }

    public void subscribeMessageLogging(MessageReceiver receiver) {
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, loggingAppSubscriptionID);
        subscribeMessage(subscriptionName, receiver);
    }

    public void subscribeMessageLabels(MessageReceiver receiver) {
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, labelsAppSubscriptionID);
        subscribeMessage(subscriptionName, receiver);
    }

    private void subscribeMessage(ProjectSubscriptionName subName, MessageReceiver receiver) {
        Subscriber subscriber = null;

        try {
            subscriber = Subscriber.newBuilder(subName, receiver)
                    .setExecutorProvider(executorProvider)
                    .build();
            subscriber.addListener(new Listener() {
                @Override
                public void failed(State from, Throwable failure) {
                    logger.log(Level.SEVERE, "Error with subscriber: " + failure.getMessage());
                }
            }, MoreExecutors.directExecutor());
            subscriber.startAsync().awaitRunning();
            logger.info("Listening for messages on " + subName);
        } catch (Exception e) {
            if (subscriber != null) {
                subscriber.stopAsync();
            }
            logger.log(Level.SEVERE, "Error during subscriber setup: " + e.getMessage(), e);
        }
    }
}

