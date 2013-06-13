package org.livingplace.messaging.activemq.api;

import javax.jms.JMSException;
import javax.jms.MessageListener;

public interface ILPPushPullClient {
    void pushPull(String requestAcceptedByTarget, MessageListener listener) throws JMSException;

    String pushPullAndWaitForAnswer(String requestAcceptedByTarget, String identifierInMsg) throws JMSException;
}
