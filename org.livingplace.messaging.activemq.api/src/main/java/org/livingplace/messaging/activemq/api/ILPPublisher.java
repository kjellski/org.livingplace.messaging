package org.livingplace.messaging.activemq.api;

public interface ILPPublisher {
    void publish(String msg);

    void disconnect();
}
