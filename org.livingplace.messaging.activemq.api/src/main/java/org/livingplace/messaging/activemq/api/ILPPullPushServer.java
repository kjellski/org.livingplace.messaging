package org.livingplace.messaging.activemq.api;

import javax.jms.JMSException;

public interface ILPPullPushServer {
    String pull() throws JMSException;

    void push(String answer);
}
