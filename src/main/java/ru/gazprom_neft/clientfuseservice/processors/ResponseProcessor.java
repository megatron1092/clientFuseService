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

import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import java.util.Arrays;

@Component
public class ResponseProcessor implements Processor {
    private static final Logger LOGGER = LogManager.getLogger(ResponseProcessor.class);

    @Autowired
    ActiveMQConnectionFactory amqConnectionFactory;

    @Override
    public void process(Exchange exchange) throws Exception {
        LOGGER.debug("Entered responseProcessor");
        String path = exchange.getIn().getHeader("CamelHttpPath", String.class);
        LOGGER.debug("Got correlationId: " + path);
        QueueConnection connection = amqConnectionFactory.createQueueConnection();
        QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        QueueReceiver receiver = session.createReceiver(new ActiveMQQueue("responses"), "JMSCorrelationID='" + path + "'");
        connection.start();
        ActiveMQMessage message = (ActiveMQMessage) receiver.receive();
        String body = Arrays.toString(message.getContent().getData());
        LOGGER.debug(String.format("Got message with id = %s and body = %s", message.getJMSMessageID(), body));
        exchange.getOut().setHeader("JMSCorrelationID", path);
        exchange.getOut().setBody(body);
        connection.stop();
    }

}
