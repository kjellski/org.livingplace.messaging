/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.livingplace.messaging.internal;

import com.mongodb.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Nachrichten ueber ein Topic senden
 * @author kjelllski
 */
public class LPPublisher {

    private TopicConnection topicConnection;
    private String topicName;
    private TopicSession topicSession;
    private Topic topic;
    private TopicPublisher publisher;
    private Mongo mongo;
    private DB db;
    private DBCollection collection;
    private ActiveMQConnectionFactory connectionFactory;
    public boolean DEBUG = false;

    public LPPublisher(String topicName) throws JMSException, UnknownHostException, MongoException {
        configure(topicName, new ConnectionSettings());
    }

    public LPPublisher(String topicName, ConnectionSettings s) throws JMSException, UnknownHostException, MongoException {
        configure(topicName, s);
    }

    /**
     * Send a TextMessage, JSON encoded, with a producer for the given Queue.
     * @param msg message content
     */
    public void publish(String msg) {
        try {
            TextMessage t = this.topicSession.createTextMessage(msg);
            this.publisher.send(topic, t);

            sendDocumentToMongoDB(t, this.topicName);
        } catch (JMSException ex) {
            Logger.getLogger(LPProducer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Connect to the CouchDB and persists the transmitted Message
     * @param msg
     */
    private void sendDocumentToMongoDB(TextMessage msg, String topicName) throws MongoException, NullPointerException {
        try {

            BasicDBObject doc = new BasicDBObject();

            doc.put("Timestamp", System.currentTimeMillis());
            doc.put("Topic:", topicName);

            doc.put("Message:", msg.getText().toString());
            doc.put("Sender:", msg.getJMSMessageID().toString());
            collection.insert(doc);

        } catch (JMSException ex) {
            Logger.getLogger(LPPublisher.class.getName()).log(Level.SEVERE, null, ex);

            System.err.println("FATAL: \t|We were trying to establish a connection to the Linving Places MongoDB, but can't reach the Database");
            System.err.println("FATAL: \t|Printing Stack:\n");
            ex.getStackTrace();

            throw new MongoException("FATAL: \t|We were trying to establish a connection to the Linving Places MongoDB, but can't reach the Database" + "\n" + ex);

        } catch (NullPointerException ex) {
            if (this.DEBUG) {
                System.out.println(Thread.currentThread().getName() + "Got null in ConcurrentPublisher.sendDocumentToMongoDB() where things are like this: " + msg + " \t|\t" + db + " " + mongo);
                ex.printStackTrace();
            }

            throw new NullPointerException();
        }
    }

    private void configure(String topicName, ConnectionSettings s) throws MongoException, JMSException, UnknownHostException {
        this.topicName = topicName;
        String address = s.amq_protocol + "://" + s.amq_ip + ":" + s.amq_port;

        this.connectionFactory = new ActiveMQConnectionFactory(address);

        try {
            this.topicConnection = this.connectionFactory.createTopicConnection();
            this.topicConnection.start();

            this.mongo = LPPersistence.getMongoInstance(s.mongo_ip, s.mongo_port);
            this.db = LPPersistence.getDBInstance();
            this.collection = db.getCollection("testCollection");

            this.topicSession = this.topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            this.topic = this.topicSession.createTopic(this.topicName);

            this.publisher = this.topicSession.createPublisher(this.topic);

            // --------------------------------------DEBUGGING--------------------------------------
            if (this.DEBUG) {
                System.out.println("Results from LPPublisher(): ");
                System.out.println("\tthis.topicConnection: " + this.topicConnection);
                System.out.println("\tthis.topicName: " + this.topicName);
                System.out.println("\tthis.mongo: " + this.mongo);
                System.out.println("\tthis.db: " + this.db);
                System.out.println("\tthis.collection: " + this.collection);
                System.out.println("\tthis.topicSession: " + this.topicSession);
                System.out.println("\tthis.topic: " + this.topic);
                System.out.println("\tthis.publisher: " + this.publisher);
            }

        } catch (JMSException ex) {
            Logger.getLogger(LPPublisher.class.getName()).log(Level.SEVERE, null, ex);

            System.err.println("FATAL: \t|We can't connect to the ActiveMQ");
            System.err.println("FATAL: \t|Printing Stack:\n");
            ex.getStackTrace();

            throw new JMSException("FATAL: \t| We can't connect to the ActiveMQ" + "\n" + ex);
        }
    }

    public void disconnect() {
        try {

            if (publisher != null) {
                this.publisher.close();
            }

            if (topicSession != null) {
                this.topicSession.close();
            }

            if (topicConnection != null) {
                this.topicConnection.close();
            }
            
        } catch (JMSException ex) {
            Logger.getLogger(LPPublisher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
