package org.livingplace.messaging.activemq.impl.internal;

import com.mongodb.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.livingplace.messaging.activemq.api.ILPConnectionSettings;
import org.livingplace.messaging.activemq.api.ILPProducer;

import javax.jms.*;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Nachrichten ueber eine Queue versenden
 *
 * @author kjelllski
 */
public class LPProducer implements ILPProducer {

    private QueueConnection queueConnection;
    private String queueName;
    private QueueSession queueSession;
    private Queue queue;
    private MessageProducer producer;
    private ActiveMQConnectionFactory connectionFactory;
    private Mongo mongo;
    private DB db;
    private DBCollection collection;
    public boolean DEBUG = false;

    public LPProducer(String queueName) throws JMSException, UnknownHostException, MongoException {
        configure(queueName, new LPConnectionSettings());
    }

    public LPProducer(String queueName, ILPConnectionSettings s) throws JMSException, UnknownHostException, MongoException {
        configure(queueName, s);
    }

    /**
     * Send a TextMessage, JSON encoded, with a producer for the given Queue.
     *
     * @param msg message content
     */
    @Override
    public void produce(String msg) {
        try {
            TextMessage t = this.queueSession.createTextMessage(msg);
            this.producer.send(queue, t);

            sendDocumentToMongoDB(t, queueName);
        } catch (JMSException ex) {
            Logger.getLogger(LPProducer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Connect to the CouchDB and persists the transmitted Message
     *
     * @param msg
     * @param queueName
     */
    @Override
    public void sendDocumentToMongoDB(TextMessage msg, String queueName) {
        try {

            BasicDBObject doc = new BasicDBObject();

            doc.put("Timestamp", System.currentTimeMillis());
            doc.put("Queue:", queueName);

            doc.put("Message:", msg.getText().toString());
            doc.put("Sender:", msg.getJMSMessageID().toString());
            collection.insert(doc);

        } catch (JMSException ex) {
            Logger.getLogger(LPProducer.class.getName()).log(Level.SEVERE, null, ex);

            System.err.println("FATAL: \t|We were trying to establish a connection to the Linving Places CouchDB, but can't reach the Database");
            System.err.println("FATAL: \t|Printing Stack:\n");
            ex.getStackTrace();

            throw new MongoException("FATAL: \t|We were trying to establish a connection to the Linving Places MongoDB, but can't reach the Database" + "\n" + ex);

        } catch (NullPointerException ex) {
            if (this.DEBUG) {
                System.out.println(Thread.currentThread().getName() + "Got null in ConcurrentProducer.sendDocumentToMongoDB() where things are like this: " + msg + " \t|\t" + db + " " + collection);
            }

            throw new NullPointerException();

        }
    }

    private void configure(String queueName, ILPConnectionSettings s) throws JMSException, MongoException, UnknownHostException {
        this.queueName = queueName;
        String address = s.getActiveMQProtocol() + "://" + s.getActiveMQIp() + ":" + s.getActiveMQPort();

        this.connectionFactory = new ActiveMQConnectionFactory(address);

        try {
            this.queueConnection = this.connectionFactory.createQueueConnection();
            this.queueConnection.start();

            this.mongo = LPPersistence.getMongoInstance(s.getMongoDBIp(), s.getMongoDBPort());
            this.db = LPPersistence.getDBInstance();
            this.collection = db.getCollection("testCollection");

            this.queueSession = this.queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

            this.queue = this.queueSession.createQueue(this.queueName);

            this.producer = this.queueSession.createProducer(this.queue);

            // --------------------------------------DEBUGGING--------------------------------------
            if (this.DEBUG) {
                System.out.println("Results from LPProducer(): ");
                System.out.println("\tthis.queueConnection: " + this.queueConnection);
                System.out.println("\tthis.queueName: " + this.queueName);
                System.out.println("\tthis.queueSession: " + this.queueSession);
                System.out.println("\tthis.queue: " + this.queue);
                System.out.println("\tthis.producer: " + this.producer);
            }

        } catch (JMSException ex) {
            Logger.getLogger(LPProducer.class.getName()).log(Level.SEVERE, null, ex);

            System.err.println("FATAL: \t|We can't connect to the ActiveMQ");
            System.err.println("FATAL: \t|Printing Stack:\n");
            ex.getStackTrace();

            throw new JMSException("FATAL: \t| We can't connect to the ActiveMQ" + "\n" + ex);
        }
    }

    @Override
    public void disconnect() {
        try {

            if (producer != null) {
                this.producer.close();
            }

            if (queueSession != null) {
                this.queueSession.close();
            }

            if (queueConnection != null) {
                this.queueConnection.close();
            }
        } catch (JMSException ex) {
            Logger.getLogger(LPConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
