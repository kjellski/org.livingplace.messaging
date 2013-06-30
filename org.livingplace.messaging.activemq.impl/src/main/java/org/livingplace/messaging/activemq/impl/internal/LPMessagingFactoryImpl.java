package org.livingplace.messaging.activemq.impl.internal;

import org.livingplace.messaging.activemq.api.*;

//@Component(immediate = true)
//@Service
public class LPMessagingFactoryImpl implements ILPMessagingFactory {
//
//    private static volatile LPMessagingFactoryImpl instance = null;
//    private static Logger logger = Logger.getLogger(LPMessagingFactoryImpl.class);
//
//    private static LPMessagingFactoryImpl createInstance() {
//        if (instance == null) {
//            synchronized (LPMessagingFactoryImpl.class){
//                if (instance == null) {
//                    logger.info("Instantiated the singleton InformationRegistry.");
//                    instance = new LPMessagingFactoryImpl();
//                }
//            }
//        }
//        return instance;
//    }
//
//    @Override
//    public LPMessagingFactoryImpl getInstance() {
//        return createInstance();
//    }

    @Override
    public ILPSubscriber getLPSubscriberInstance() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ILPPublisher getLPPusblisher() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ILPConsumer getLPConsumer() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ILPProducer getLPProduce() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ILPPullPushServer getLPPullPushServer() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ILPPushPullClient getLPPushPullClient() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ILPSubscriber getLPSubscriberInstance(ILPConnectionSettings settings) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ILPPublisher getLPPusblisher(ILPConnectionSettings settings) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ILPConsumer getLPConsumer(ILPConnectionSettings settings) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ILPProducer getLPProduce(ILPConnectionSettings settings) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ILPPullPushServer getLPPullPushServer(ILPConnectionSettings settings) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ILPPushPullClient getLPPushPullClient(ILPConnectionSettings settings) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
