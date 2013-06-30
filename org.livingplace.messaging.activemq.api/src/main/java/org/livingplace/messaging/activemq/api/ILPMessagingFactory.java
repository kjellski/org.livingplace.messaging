package org.livingplace.messaging.activemq.api;

import javax.jms.JMSException;
import java.net.UnknownHostException;

public interface ILPMessagingFactory {
    ILPMessagingFactory getInstance();

    ILPConnectionSettings createLPConnectionSettings();

    ILPSubscriber createLPSubscriberInstance(String topic) throws JMSException;
    ILPPublisher createLPPusblisher(String topic) throws JMSException, UnknownHostException;

    ILPConsumer createLPConsumer(String queue) throws JMSException;
    ILPProducer createLPProducer(String queue) throws JMSException, UnknownHostException;

    ILPPullPushServer createLPPullPushServer(String queue, String topic) throws JMSException, UnknownHostException;
    ILPPushPullClient createLPPushPullClient(String queue, String topic) throws JMSException, UnknownHostException;

    ILPSubscriber createLPSubscriberInstance(String topic, ILPConnectionSettings settings) throws JMSException;
    ILPPublisher createLPPusblisher(String topic, ILPConnectionSettings settings) throws JMSException, UnknownHostException;

    ILPConsumer createLPConsumer(String queue, ILPConnectionSettings settings) throws JMSException;
    ILPProducer createLPProducer(String queue, ILPConnectionSettings settings) throws JMSException, UnknownHostException;

    ILPPullPushServer createLPPullPushServer(String queue, String topic, ILPConnectionSettings settings) throws JMSException, UnknownHostException;
    ILPPushPullClient createLPPushPullClient(String queue, String topic, ILPConnectionSettings settings) throws JMSException, UnknownHostException;
}
