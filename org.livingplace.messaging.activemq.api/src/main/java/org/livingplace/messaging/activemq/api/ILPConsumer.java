package org.livingplace.messaging.activemq.api;

import javax.jms.JMSException;
import javax.jms.MessageListener;

public interface ILPConsumer {
    void consume(MessageListener listener) throws JMSException;

    String consumeBlocking() throws JMSException;

    String consumeBlockingWithTimeout(long timeoutInMillies)
            throws JMSException;

    void disconnect();
}
