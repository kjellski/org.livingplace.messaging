package org.livingplace.messaging.activemq.api;

import javax.jms.TextMessage;

public interface ILPProducer {
    void produce(String msg);

    void sendDocumentToMongoDB(TextMessage msg, String queueName);

    void disconnect();
}
