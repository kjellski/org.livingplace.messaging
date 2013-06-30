package org.livingplace.messaging.activemq.api;

public interface ILPMessagingFactory {


    ILPSubscriber getLPSubscriberInstance();
    ILPPublisher getLPPusblisher();

    ILPConsumer getLPConsumer();
    ILPProducer getLPProduce();

    ILPPullPushServer getLPPullPushServer();
    ILPPushPullClient getLPPushPullClient();

    ILPSubscriber getLPSubscriberInstance(ILPConnectionSettings settings);
    ILPPublisher getLPPusblisher(ILPConnectionSettings settings);

    ILPConsumer getLPConsumer(ILPConnectionSettings settings);
    ILPProducer getLPProduce(ILPConnectionSettings settings);

    ILPPullPushServer getLPPullPushServer(ILPConnectionSettings settings);
    ILPPushPullClient getLPPushPullClient(ILPConnectionSettings settings);
}
