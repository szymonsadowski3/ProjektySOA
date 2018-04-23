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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Logger;
import java.util.Properties;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class QueueManager {
    private static final Logger log = Logger.getLogger(QueueManager.class.getName());

    // Set up all the default values
    private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    private static final String DEFAULT_DESTINATION = "jms/queue/webQueue";
    private static final String DEFAULT_USERNAME = "quickstartUser";
    private static final String DEFAULT_PASSWORD = "quickstartPwd1!";
    private static final String INITIAL_CONTEXT_FACTORY = "org.wildfly.naming.client.WildFlyInitialContextFactory";
    private static final String PROVIDER_URL = "http-remoting://127.0.0.1:8080";

    private JMSContext context;
    private Destination destination;
    private QueueBrowser browser;
    private JMSProducer producer;

    public QueueManager() throws JMSException {
        Context namingContext = null;

        try {
            String userName = System.getProperty("username", DEFAULT_USERNAME);
            String password = System.getProperty("password", DEFAULT_PASSWORD);

            final Properties env = new Properties();
            setUpNamingContext(userName, password, env);
            namingContext = new InitialContext(env);

            ConnectionFactory connectionFactory = performJndiLookups(namingContext);
            Connection connection = connectionFactory.createConnection(DEFAULT_USERNAME, DEFAULT_PASSWORD);

            this.context = connectionFactory.createContext(userName, password);

            Queue queue = this.context.createQueue("webQueue");

            this.destination = getDestination(namingContext);
            this.browser = context.createBrowser(queue);
            this.producer = context.createProducer();
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

    public ArrayList<String> fetchMessagesFromQueue() throws JMSException {
        ArrayList<String> messages = new ArrayList<>();

        Enumeration msgs = browser.getEnumeration();

        if (msgs.hasMoreElements()) {
            while (msgs.hasMoreElements()) {
                Message tempMsg = (Message)msgs.nextElement();
                TextMessage textMessage = (TextMessage) tempMsg;
                messages.add(textMessage.getText());
            }
        }

        return messages;
    }

    public void putMessageInQueue(String message) {
        this.producer.send(destination, message);
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

    public static void main(String[] args) throws JMSException {
        QueueManager mngr = new QueueManager();
//        mngr.putMessageInQueue(MessageFormat.format("Name: {0} | Address: {1} | Phone Number: {2}", "Google", "Silicon valley", "7777"));
        System.out.println(mngr.fetchMessagesFromQueue());
    }
}
