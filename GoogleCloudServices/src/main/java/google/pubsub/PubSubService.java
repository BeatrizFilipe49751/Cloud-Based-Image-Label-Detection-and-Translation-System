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
import com.google.cloud.Timestamp;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.*;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import java.io.IOException;
import java.util.List;
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

            Timestamp timestamp = Timestamp.now();

            // Create Pub/Sub message
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                    .setData(data)
                    .putAttributes("timestamp", timestamp.toString())
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

    public void pullMessagesWorkQueue(String subscriptionId, java.util.function.Consumer<PubsubMessage> messageConsumer) throws IOException {
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);

        try (SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create()) {
            while (true) {
                PullRequest pullRequest = PullRequest.newBuilder()
                        .setMaxMessages(10)
                        .setSubscription(subscriptionName.toString())
                        .build();

                PullResponse pullResponse = subscriptionAdminClient.pullCallable().call(pullRequest);

                List<ReceivedMessage> receivedMessages = pullResponse.getReceivedMessagesList();

                for (ReceivedMessage receivedMessage : receivedMessages) {
                    messageConsumer.accept(receivedMessage.getMessage());

                    // Acknowledge the received message
                    AcknowledgeRequest ackRequest = AcknowledgeRequest.newBuilder()
                            .setSubscription(subscriptionName.toString())
                            .addAckIds(receivedMessage.getAckId())
                            .build();
                    subscriptionAdminClient.acknowledgeCallable().call(ackRequest);
                }
            }
        }
    }
}

