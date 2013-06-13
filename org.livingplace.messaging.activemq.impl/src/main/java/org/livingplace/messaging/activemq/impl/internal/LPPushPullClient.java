package org.livingplace.messaging.activemq.impl.internal;

import org.livingplace.messaging.activemq.api.ILPPushPullClient;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import java.net.UnknownHostException;

/**
 * @author kjelllski
 */
public class LPPushPullClient implements ILPPushPullClient {

    private LPProducer producer;
    private LPSubscriber subscriber;

    private boolean DEBUG;

    public boolean isDEBUG() {
        return DEBUG;
    }

    public void setDEBUG(boolean DEBUG) {
        this.producer.DEBUG = DEBUG;
        this.subscriber.DEBUG = DEBUG;
        this.DEBUG = DEBUG;
    }

    public LPPushPullClient(String queueName, String topicName) throws JMSException, UnknownHostException {
        configure(queueName, topicName, new ConnectionSettings());
    }

    public LPPushPullClient(String queueName, String topicName, ConnectionSettings s) throws JMSException, UnknownHostException {
        configure(queueName, topicName, s);
    }

    /**
     * This sets the MessageListener for the internal subscriber after producing
     * the users request on the given queue
     *
     * @param requestAcceptedByTarget sent to corresponding queue
     * @param listener                for the topic where the answer comes from
     */
    @Override
    public void pushPull(String requestAcceptedByTarget, MessageListener listener) throws JMSException {
        this.producer.produce(requestAcceptedByTarget);
        this.subscriber.subscribe(listener);
    }


    /**
     * This sends the users request to the given queue, and loops until it finds
     * the "identifierInMsg" on the corresponding topic
     *
     * @param requestAcceptedByTarget
     * @param identifierInMsg
     * @return the msg that contains the identifierInMsg pattern.
     */
    @Override
    public String pushPullAndWaitForAnswer(String requestAcceptedByTarget, String identifierInMsg) throws JMSException {
        this.producer.produce(requestAcceptedByTarget);

        String msg = "";

        while (msg != null) {
            msg = this.subscriber.subscribeBlocking();

            if (msg != null && msg.contains(identifierInMsg))
                return msg;
        }

        return null;
    }

    private void configure(String queueName, String topicName, ConnectionSettings s) throws JMSException, UnknownHostException {
        this.producer = new LPProducer(queueName, s);
        this.subscriber = new LPSubscriber(topicName, s);
    }
}
