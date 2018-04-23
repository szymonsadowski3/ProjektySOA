/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.jms;

import java.util.logging.Logger;
import java.util.Properties;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class HelloWorldJMSClient {
    private static final Logger log = Logger.getLogger(HelloWorldJMSClient.class.getName());

    // Set up all the default values
    private static final String DEFAULT_MESSAGE = "Hello, World!";
    private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
//    private static final String DEFAULT_DESTINATION = "jms/queue/test";
    private static final String DEFAULT_DESTINATION = "jms/topic/sport";
    private static final String DEFAULT_MESSAGE_COUNT = "1";
    private static final String DEFAULT_USERNAME = "quickstartUser";
    private static final String DEFAULT_PASSWORD = "quickstartPwd1!";
    private static final String INITIAL_CONTEXT_FACTORY = "org.wildfly.naming.client.WildFlyInitialContextFactory";
    private static final String PROVIDER_URL = "http-remoting://127.0.0.1:8080";

    public static void main(String[] args) throws JMSException, InterruptedException {

        Context namingContext = null;

        try {
            String userName = System.getProperty("username", DEFAULT_USERNAME);
            String password = System.getProperty("password", DEFAULT_PASSWORD);

            final Properties env = new Properties();
            setUpNamingContext(userName, password, env);
            namingContext = new InitialContext(env);

            ConnectionFactory connectionFactory = performJndiLookups(namingContext);
            Connection connection = connectionFactory.createConnection(DEFAULT_USERNAME, DEFAULT_PASSWORD);

            Destination destination = getDestination(namingContext);

            int count = getMessageCount();
            String content = System.getProperty("message.content", DEFAULT_MESSAGE);

            JMSContext context = connectionFactory.createContext(userName, password);

            Topic topicSport = context.createTopic("Sport");
            Topic topicBooks = context.createTopic("Books");

            JMSConsumer sportConsumer1 = context.createConsumer(topicSport, "Category = 'All'");
            sportConsumer1.setMessageListener(message -> {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("Consumer sportConsumer1 received message: " + textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });

            JMSConsumer sportConsumer2 = context.createConsumer(topicSport,"Category = 'All'");
            sportConsumer2.setMessageListener(message -> {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("Consumer sportConsumer2 received message: " + textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });

            JMSConsumer volleyballConsumer = context.createConsumer(topicSport, "Category = 'Volleyball' OR Category = 'All'");
            volleyballConsumer.setMessageListener(message -> {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("Consumer volleyballConsumer received message: " + textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });

            JMSConsumer booksConsumer = context.createConsumer(topicBooks);
            booksConsumer.setMessageListener(message -> {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("Consumer booksConsumer received message: " + textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });

            JMSProducer producer = context.createProducer();

            TextMessage message1 = context.createTextMessage();
            message1.setObjectProperty("Category", "All");
            message1.setText("Barcelona won the cup");
            producer.send(topicSport, message1);

            Thread.sleep(1000);

            TextMessage message2 = context.createTextMessage();
            message2.setObjectProperty("Category", "All");
            message2.setText("Arsenal won the league match");
            producer.send(topicSport, message2);

            Thread.sleep(1000);

            TextMessage message3 = context.createTextMessage();
            message3.setObjectProperty("Category", "Volleyball");
            message3.setText("2018 NC men's volleyball championship field to be revealed Sunday");
            producer.send(topicSport, message3);

            Thread.sleep(1000);

            producer.send(topicBooks, "New Witcher book is going to be written");

            Thread.sleep(1000);


        } catch (NamingException e) {
            log.severe(e.getMessage());
        } finally {
            if (namingContext != null) {
                try {
                    namingContext.close();
                } catch (NamingException e) {
                    log.severe(e.getMessage());
                }
            }
        }
    }

    private static int getMessageCount() {
        return Integer.parseInt(System.getProperty("message.count", DEFAULT_MESSAGE_COUNT));
    }

    private static Destination getDestination(Context namingContext) throws NamingException {
        String destinationString = System.getProperty("destination", DEFAULT_DESTINATION);
        log.info("Attempting to acquire destination \"" + destinationString + "\"");
        Destination destination = (Destination) namingContext.lookup(destinationString);
        log.info("Found destination \"" + destinationString + "\" in JNDI");
        return destination;
    }

    private static void setUpNamingContext(String userName, String password, Properties env) {
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
        env.put(Context.SECURITY_PRINCIPAL, userName);
        env.put(Context.SECURITY_CREDENTIALS, password);
    }

    private static ConnectionFactory performJndiLookups(Context namingContext) throws NamingException {
        String connectionFactoryString = System.getProperty("connection.factory", DEFAULT_CONNECTION_FACTORY);
        log.info("Attempting to acquire connection factory \"" + connectionFactoryString + "\"");
        ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup(connectionFactoryString);
        log.info("Found connection factory \"" + connectionFactoryString + "\" in JNDI");
        return connectionFactory;
    }
}
