package org.livingplace.messaging.activemq.impl.internal;

import org.livingplace.messaging.activemq.api.ILPConnectionSettings;

/**
 * @author kjelllski
 */
public class LPConnectionSettings implements ILPConnectionSettings {
    @Override
    public String getActiveMQProtocol() {
        return ActiveMQProtocol;
    }

    @Override
    public void setActiveMQProtocol(String activeMQProtocol) {
        ActiveMQProtocol = activeMQProtocol;
    }

    @Override
    public String getActiveMQIp() {
        return ActiveMQIp;
    }

    @Override
    public void setActiveMQIp(String activeMQIp) {
        ActiveMQIp = activeMQIp;
    }

    @Override
    public String getActiveMQPort() {
        return ActiveMQPort;
    }

    @Override
    public void setActiveMQPort(String activeMQPort) {
        ActiveMQPort = activeMQPort;
    }

    @Override
    public String getMongoDBIp() {
        return MongoDBIp;
    }

    @Override
    public void setMongoDBIp(String mongoDBIp) {
        MongoDBIp = mongoDBIp;
    }

    @Override
    public int getMongoDBPort() {
        return MongoDBPort;
    }

    @Override
    public void setMongoDBPort(int mongoDBPort) {
        MongoDBPort = mongoDBPort;
    }

    /**
     * Protocol used to connect to the ActiveMQ.
     */
    private String ActiveMQProtocol = "tcp";
    /**
     * IP used to connect to the ActiveMQ.
     */
    private String ActiveMQIp = "127.0.0.1";
    /**
     * Port used to connect to the ActiveMQ.
     */
    private String ActiveMQPort = "61616";
    /**
     * IP used to connect to the MongoDB.
     */
    private String MongoDBIp = "127.0.0.1";
    /**
     * Port used to connect to the MongoDB.
     */
    private int MongoDBPort = 27017;
}
