package org.livingplace.messaging.activemq.api;

public interface ILPConnectionSettings {
    String getActiveMQProtocol();
    void setActiveMQProtocol(String activeMQProtocol);

    String getActiveMQIp();
    void setActiveMQIp(String activeMQIp);

    String getActiveMQPort();
    void setActiveMQPort(String activeMQPort);

    String getMongoDBIp();
    void setMongoDBIp(String mongoDBIp);

    int getMongoDBPort();
    void setMongoDBPort(int mongoDBPort);
}
