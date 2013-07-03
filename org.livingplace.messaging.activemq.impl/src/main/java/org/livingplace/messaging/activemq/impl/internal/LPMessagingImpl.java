package org.livingplace.messaging.activemq.impl.internal;

import org.livingplace.messaging.activemq.api.*;

import javax.jms.JMSException;
import java.net.UnknownHostException;

public class LPMessagingImpl implements ILPMessaging {
    @Override
    public ILPConnectionSettings createLPConnectionSettings() {
        return new LPConnectionSettings();
    }

    @Override
    public ILPSubscriber createLPSubscriberInstance(String topic) throws JMSException {
        return createLPSubscriberInstance(topic, new LPConnectionSettings());
    }

    @Override
    public ILPPublisher createLPPusblisher(String topic) throws JMSException, UnknownHostException {
        return createLPPusblisher(topic, new LPConnectionSettings());
    }

    @Override
    public ILPConsumer createLPConsumer(String queue) throws JMSException {
        return createLPConsumer(queue, new LPConnectionSettings());
    }

    @Override
    public ILPProducer createLPProducer(String queue) throws JMSException, UnknownHostException {
        return createLPProducer(queue, new LPConnectionSettings());
    }

    @Override
    public ILPPullPushServer createLPPullPushServer(String queue, String topic) throws JMSException, UnknownHostException {
        return createLPPullPushServer(queue, topic, new LPConnectionSettings());
    }

    @Override
    public ILPPushPullClient createLPPushPullClient(String queue, String topic) throws JMSException, UnknownHostException {
        return createLPPushPullClient(queue, topic, new LPConnectionSettings());
    }

    @Override
    public ILPSubscriber createLPSubscriberInstance(String topic, ILPConnectionSettings settings) throws JMSException {
        return new LPSubscriber(topic, settings);
    }

    @Override
    public ILPPublisher createLPPusblisher(String topic, ILPConnectionSettings settings) throws JMSException, UnknownHostException {
        return new LPPublisher(topic, settings);
    }

    @Override
    public ILPConsumer createLPConsumer(String queue, ILPConnectionSettings settings) throws JMSException {
        return new LPConsumer(queue, settings);
    }

    @Override
    public ILPProducer createLPProducer(String queue, ILPConnectionSettings settings) throws JMSException, UnknownHostException {
        return new LPProducer(queue, settings);
    }

    @Override
    public ILPPullPushServer createLPPullPushServer(String queue, String topic, ILPConnectionSettings settings) throws JMSException, UnknownHostException {
        return new LPPullPushServer(queue, topic, settings);
    }

    @Override
    public ILPPushPullClient createLPPushPullClient(String queue, String topic, ILPConnectionSettings settings) throws JMSException, UnknownHostException {
        return new LPPushPullClient(queue, topic, settings);
    }
}
