package org.livingplace.messaging.activemq.impl.internal;

import com.mongodb.MongoException;
import org.livingplace.messaging.activemq.api.ILPConnectionSettings;
import org.livingplace.messaging.activemq.api.ILPPullPushServer;

import javax.jms.JMSException;
import java.net.UnknownHostException;

public class LPPullPushServer implements ILPPullPushServer {

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
        configure(queueName, topicName, new LPConnectionSettings());
    }

    public LPPullPushServer(String queueName, String topicName, ILPConnectionSettings s) throws MongoException, JMSException, UnknownHostException {
        configure(queueName, topicName, s);
    }

    @Override
    public String pull() throws JMSException {
        return this.consumer.consumeBlocking();
    }

    @Override
    public void push(String answer) {
        this.publisher.publish(answer);
    }

    private void configure(String queueName, String topicName, ILPConnectionSettings s) throws JMSException, MongoException, UnknownHostException {
        this.consumer = new LPConsumer(queueName, s);
        this.publisher = new LPPublisher(topicName, s);
    }
}
