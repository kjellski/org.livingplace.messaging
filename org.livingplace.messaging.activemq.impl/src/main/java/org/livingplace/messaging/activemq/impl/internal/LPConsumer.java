package org.livingplace.messaging.activemq.impl.internal;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Consumer for queues
 *
 * @author kjelllski
 */
public class LPConsumer {

    private QueueConnection queueConnection;
    private String queueName;
    private QueueSession queueSession;
    private Queue queue;
    private MessageConsumer consumer;
    private ActiveMQConnectionFactory connectionFactory;
    private String global_ip;
    private String global_port;
    private String global_protocol;
    private String address;
    public boolean DEBUG = false;

    public LPConsumer(String queueName) throws JMSException {
        configure(queueName, new ConnectionSettings());
    }

    public LPConsumer(String queueName, ConnectionSettings s)
            throws JMSException {
        configure(queueName, s);
    }

    /**
     * Adds a MessageListener to a given Queue in order to receive TextMessages.
     *
     * @param listener MessageListener with an implementation of onMessage()
     */
    public void consume(MessageListener listener) throws JMSException {
        try {
            this.consumer.setMessageListener(listener);
        } catch (JMSException ex) {
            Logger.getLogger(LPConsumer.class.getName()).log(Level.SEVERE,
                    null, ex);

            System.err
                    .println("FATAL: \t|We were trying to add a MessageListener to a consumer for the queue: "
                            + queueName);
            System.err.println("FATAL: \t|Printing Stack:\n");
            ex.getStackTrace();

            throw new JMSException(
                    "FATAL: \t|We were trying to add a MessageListener to a consumer for the queue: "
                            + queueName + "\n" + ex);
        }
    }

    /**
     * Receives a TextMessage from the given queueName.
     *
     * @return Message from the queue. null indicates failure of receive, look
     *         at System.err and Logger file for further instructions.
     */
    public String consumeBlocking() throws JMSException {
        String type = "unknown";
        try {
            Message msg = this.consumer.receive();

            type = msg.getJMSType();

            if (msg instanceof TextMessage) {
                return ((TextMessage) msg).getText();
            } else {
                return null;
            }

        } catch (JMSException ex) {
            Logger.getLogger(LPConsumer.class.getName()).log(Level.SEVERE,
                    null, ex);

            System.err
                    .println("FATAL: \t|We recveived an unsupported message from type \""
                            + type + "\" on the queue: " + queueName);
            System.err.println("FATAL: \t|Printing Stack:\n");
            ex.getStackTrace();

            throw new JMSException(
                    "FATAL: \t|We recveived an unsupported message from type \""
                            + type + "\" on the queue: " + queueName + "\n"
                            + ex);
        }

    }

    /**
     * Receives a TextMessage from the given queueName and returnes after
     * timeoutInMillies if nothing was received.
     *
     * @param timeoutInMillies timeout to wait before returning null
     * @return Message from the queue. null indicates failure or timout of
     *         receive, look at System.err and Logger file for further
     *         instructions.
     */
    public String consumeBlockingWithTimeout(long timeoutInMillies)
            throws JMSException {
        String type = "unknown";
        try {
            Message msg = this.consumer.receive(timeoutInMillies);
            if (msg != null) {
                type = msg.getJMSType();

                if (msg instanceof TextMessage) {
                    return ((TextMessage) msg).getText();
                }

            }
            return null;
        } catch (JMSException ex) {
            Logger.getLogger(LPConsumer.class.getName()).log(Level.SEVERE,
                    null, ex);

            System.err
                    .println("FATAL: \t|We recveived an unsupported message from type \""
                            + type + "\" on the queue: " + queueName);
            System.err.println("FATAL: \t|Printing Stack:\n");
            ex.getStackTrace();

            throw new JMSException(
                    "FATAL: \t|We recveived an unsupported message from type \""
                            + type + "\" on the queue: " + queueName + "\n"
                            + ex);
        }
    }

    private void configure(String queueName, ConnectionSettings s)
            throws JMSException {
        this.queueName = queueName;
        global_ip = s.amq_ip;
        global_port = s.amq_port;
        global_protocol = s.amq_protocol;
        String address = s.amq_protocol + "://" + s.amq_ip + ":" + s.amq_port;

        this.connectionFactory = new ActiveMQConnectionFactory(address);

        try {
            this.queueConnection = this.connectionFactory
                    .createQueueConnection();
            this.queueConnection.start();

            this.queueSession = this.queueConnection.createQueueSession(false,
                    Session.AUTO_ACKNOWLEDGE);

            this.queue = this.queueSession.createQueue(this.queueName);

            this.consumer = this.queueSession.createConsumer(this.queue);

            // --------------------------------------DEBUGGING--------------------------------------
            if (this.DEBUG) {
                System.out.println("Results from LPConsumer(): ");
                System.out.println("\tthis.queueConnection: "
                        + this.queueConnection);
                System.out.println("\tthis.queueName: " + this.queueName);
                System.out.println("\tthis.queueSession: " + this.queueSession);
                System.out.println("\tthis.queue: " + this.queue);
                System.out.println("\tthis.consumer: " + this.consumer);
            }

        } catch (JMSException ex) {
            Logger.getLogger(LPConsumer.class.getName()).log(Level.SEVERE,
                    null, ex);

            System.err.println("FATAL: \t|We can't connect to the ActiveMQ");
            System.err.println("FATAL: \t|Printing Stack:\n");
            ex.getStackTrace();

            throw new JMSException(
                    "FATAL: \t| We can't connect to the ActiveMQ" + "\n" + ex);
        }
    }

    public void disconnect() {
        try {

            if (consumer != null) {
                this.consumer.close();
            }

            if (queueSession != null) {
                this.queueSession.close();
            }

            if (queueConnection != null) {
                this.queueConnection.close();
            }
        } catch (JMSException ex) {
            Logger.getLogger(LPConsumer.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }
}
