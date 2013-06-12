package org.livingplace.messaging.activemq.impl.internal;

/**
 * @author kjelllski
 */
public class ConnectionSettings {
    /**
     * Protocol used to connect to the ActiveMQ.
     */
    public String amq_protocol = "tcp";
    /**
     * IP used to connect to the ActiveMQ.
     */
    public String amq_ip = "127.0.0.1";
    /**
     * Port used to connect to the ActiveMQ.
     */
    public String amq_port = "61616";
    /**
     * IP used to connect to the MongoDB.
     */
    public String mongo_ip = "127.0.0.1";
    /**
     * Port used to connect to the MongoDB.
     */
    public int mongo_port = 27017;
}
