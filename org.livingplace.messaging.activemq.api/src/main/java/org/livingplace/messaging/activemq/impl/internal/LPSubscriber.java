/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.livingplace.messaging.internal;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author kjelllski
 */
public class LPSubscriber {

	private TopicConnection topicConnection;
	private String topicName;
	private TopicSession topicSession;
	private Topic topic;
	private TopicSubscriber subscriber;
	private ActiveMQConnectionFactory connectionFactory;
	private String global_ip;
	private String global_port;
	private String global_protocol;
	private String address;
	public boolean DEBUG = false;

	public LPSubscriber(String topicName) throws JMSException {
		configure(topicName, new ConnectionSettings());
	}

	public LPSubscriber(String topicName, ConnectionSettings s)
			throws JMSException {
		configure(topicName, s);
	}

	/**
	 * Adds a MessageListener to a given Topic in order to receive TextMessages.
	 * 
	 * @param listener
	 *            MessageListener with an implementation of onMessage()
	 */
	public void subscribe(MessageListener listener) throws JMSException {
		try {
			this.subscriber.setMessageListener(listener);
		} catch (JMSException ex) {
			Logger.getLogger(LPConsumer.class.getName()).log(Level.SEVERE,
					null, ex);

			System.err
					.println("FATAL: \t|We were trying to add a MessageListener to a subscriber for the topic: "
							+ topicName);
			System.err.println("FATAL: \t|Printing Stack:\n");
			ex.getStackTrace();

			throw new JMSException(
					"FATAL: \t|We were trying to add a MessageListener to a consumer for the queue: "
							+ topicName + "\n" + ex);
		}
	}

	/**
	 * Receives a TextMessage from the given topicName.
	 * 
	 * @return Message from the topic. null indicates failure of receive, look
	 *         at System.err and Logger file for further instructions.
	 */
	public String subscribeBlocking() throws JMSException {
		String type = "unknown";
		try {
			Message msg = this.subscriber.receive();
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
							+ type + "\" on the topic: " + topicName);
			System.err.println("FATAL: \t|Printing Stack:\n");
			ex.getStackTrace();

			throw new JMSException(
					"FATAL: \t|We recveived an unsupported message from type \""
							+ type + "\" on the queue: " + topicName + "\n"
							+ ex);
		}
	}

	/**
	 * Receives a TextMessage from the given topicName and returnes after
	 * timeoutInMillies if nothing was received.
	 * 
	 * @param timeoutInMillies
	 *            timeout to wait before returning null
	 * @return Message from the topic. null indicates failure or timeout of
	 *         receive, look at System.err and Logger file for further
	 *         instructions.
	 */
	public String subscribeBlockingWithTimeout(long timeoutInMillies)
			throws InterruptedException, JMSException {
		String type = "unknown";
		try {
			Message msg = this.subscriber.receive(timeoutInMillies);
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
							+ type + "\" on the queue: " + topicName);
			System.err.println("FATAL: \t|Printing Stack:\n");
			ex.getStackTrace();

			throw new JMSException(
					"FATAL: \t|We recveived an unsupported message from type \""
							+ type + "\" on the queue: " + topicName + "\n"
							+ ex);
		}
	}

	private void configure(String topicName, ConnectionSettings s)
			throws JMSException {
		this.topicName = topicName;
		global_ip = s.amq_ip;
		global_port = s.amq_port;
		global_protocol = s.amq_protocol;
		address = global_protocol + "://" + global_ip + ":" + global_port;

		this.connectionFactory = new ActiveMQConnectionFactory(address);

		try {
			this.topicConnection = this.connectionFactory
					.createTopicConnection();
			this.topicConnection.start();

			this.topicSession = this.topicConnection.createTopicSession(false,
					Session.AUTO_ACKNOWLEDGE);

			this.topic = this.topicSession.createTopic(this.topicName);

			this.subscriber = this.topicSession.createSubscriber(this.topic);

			// --------------------------------------DEBUGGING--------------------------------------
			if (this.DEBUG) {
				System.out.println("Results from LPSubscriber(): ");
				System.out.println("\tthis.topicConnection: "
						+ this.topicConnection);
				System.out.println("\tthis.topicName: " + this.topicName);
				System.out.println("\tthis.topicSession: " + this.topicSession);
				System.out.println("\tthis.topic: " + this.topic);
				System.out.println("\tthis.subscriber: " + this.subscriber);
			}

		} catch (JMSException ex) {
			Logger.getLogger(LPProducer.class.getName()).log(Level.SEVERE,
					null, ex);

			System.err
					.println("FATAL: \t|We were trying to add a MessageListener to a subscriber for the topic: "
							+ this.topicName);
			System.err.println("FATAL: \t|Printing Stack:\n");
			ex.getStackTrace();

			throw new JMSException(
					"FATAL: \t| We can't connect to the ActiveMQ" + "\n" + ex);
		}
	}

	public void disconnect() {
		try {

			if (subscriber != null) {
				this.subscriber.close();
			}

			if (topicSession != null) {
				this.topicSession.close();
			}

			if (topicConnection != null) {
				this.topicConnection.close();
			}
		} catch (JMSException ex) {
			Logger.getLogger(LPSubscriber.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
}
