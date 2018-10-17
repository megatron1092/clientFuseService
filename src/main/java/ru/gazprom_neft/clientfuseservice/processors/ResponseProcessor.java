package ru.gazprom_neft.clientfuseservice.processors;// Created by IntelliJ IDEA.
// User: Sergey Telitsyn
// Date: 17.10.2018
// Time: 14:23

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.UUID;

public class ResponseProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        String path = exchange.getIn().getHeader("CamelHttpPath", String.class);
        exchange.getIn().setBody(path, String.class);
        String correlationId = UUID.randomUUID().toString();
        exchange.getIn().setHeader("JMSCorrelationID", correlationId);
        exchange.getOut().setBody(correlationId);
        exchange.getOut().setHeader("JMSCorrelationID", correlationId);
    }
}
