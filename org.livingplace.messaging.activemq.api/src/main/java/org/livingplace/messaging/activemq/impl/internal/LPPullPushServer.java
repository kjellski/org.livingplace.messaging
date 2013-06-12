package org.livingplace.messaging.internal;

import com.mongodb.MongoException;

import javax.jms.JMSException;
import java.net.UnknownHostException;

public class LPPullPushServer {

    private LPConsumer consumer;
    private LPPublisher publisher;

    private boolean DEBUG;

    public boolean isDEBUG() {
        return DEBUG;
    }

    public void setDEBUG(boolean DEBUG) {
        this.consumer.DEBUG = DEBUG;
        this.publisher.DEBUG = DEBUG;
        this.DEBUG = DEBUG;
    }

    public LPPullPushServer(String queueName, String topicName) throws MongoException, UnknownHostException, JMSException {
        configure(queueName, topicName, new ConnectionSettings());
    }

    public LPPullPushServer(String queueName, String topicName, ConnectionSettings s) throws MongoException, JMSException, UnknownHostException {
        configure(queueName, topicName, s);
    }

    public String pull() throws JMSException {
        return this.consumer.consumeBlocking();
    }

    public void push(String answer) {
        this.publisher.publish(answer);
    }

    private void configure(String queueName, String topicName, ConnectionSettings s) throws JMSException, MongoException, UnknownHostException {
        this.consumer = new LPConsumer(queueName, s);
        this.publisher = new LPPublisher(topicName, s);
    }
}
