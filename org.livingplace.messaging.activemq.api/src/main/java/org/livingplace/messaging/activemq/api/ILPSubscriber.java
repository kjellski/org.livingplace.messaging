package org.livingplace.messaging.activemq.api;

import javax.jms.JMSException;
import javax.jms.MessageListener;

public interface ILPSubscriber {
    void subscribe(MessageListener listener) throws JMSException;

    String subscribeBlocking() throws JMSException;

    String subscribeBlockingWithTimeout(long timeoutInMillies)
            throws InterruptedException, JMSException;

    void disconnect();
}
