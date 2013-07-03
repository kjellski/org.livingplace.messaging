package org.livingplace.messaging.activemq.impl.internal;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.log4j.Logger;
import org.livingplace.messaging.activemq.api.ILPMessaging;
import org.livingplace.messaging.activemq.api.ILPMessagingFactory;

@Service(serviceFactory = true)
@Component
public class LPMessagingFactoryImpl implements ILPMessagingFactory {

    private static volatile ILPMessaging instance = null;
    private static Logger logger = Logger.getLogger(LPMessagingFactoryImpl.class);

    private static ILPMessaging createInstance() {
        if (instance == null) {
            synchronized (LPMessagingFactoryImpl.class){
                if (instance == null) {
                    logger.info("Instantiated the singleton InformationRegistry.");
                    instance = new LPMessagingImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public ILPMessaging getInstance() {
        return createInstance();
    }
}
