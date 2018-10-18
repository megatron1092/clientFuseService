package ru.gazprom_neft.clientfuseservice.processors;// Created by IntelliJ IDEA.
// User: Sergey Telitsyn
// Date: 17.10.2018
// Time: 14:23

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.Arrays;

@Component
public class ResponseProcessor implements Processor {
    private static final Logger LOGGER = LogManager.getLogger(ResponseProcessor.class);

    @Autowired
    ActiveMQConnectionFactory amqConnectionFactory;

    @Override
    public void process(Exchange exchange) throws Exception {
        LOGGER.debug("Entered responseProcessor");
        System.out.println("Entered responseProcessor");
        String path = exchange.getIn().getHeader("CamelHttpPath", String.class);
        LOGGER.debug("Got correlationId: " + path);
        System.out.println("Got correlationId: " + path);
        QueueConnection connection = amqConnectionFactory.createQueueConnection();
        QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        QueueReceiver receiver = session.createReceiver(new ActiveMQQueue("responses"), "JMSCorrelationID='" + path + "'");
        exchange.getOut().setHeader("JMSCorrelationID", path);
        connection.start();
        System.out.println("Opened connection");
        try{
            ActiveMQMessage message = (ActiveMQMessage) receiver.receive();
            System.out.println("message = "+message);
            String body = Arrays.toString(message.getContent().getData());
            System.out.println("message boyd: "+ body);
            LOGGER.debug(String.format("Got message with id = %s and body = %s", message.getJMSMessageID(), body));
            exchange.getOut().setBody(body);
        } catch (JMSException ex) {
            LOGGER.error("Error while obtaining message: " + ex.getMessage());
            System.out.println("Error while obtaining message: " + ex.getMessage());
            exchange.getOut().setBody("Error while obtaining message");
        }
        connection.stop();
        System.out.println("Stopped connection");
    }

}
