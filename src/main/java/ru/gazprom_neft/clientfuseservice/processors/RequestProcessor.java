package ru.gazprom_neft.clientfuseservice.processors;// Created by IntelliJ IDEA.
// User: Sergey Telitsyn
// Date: 17.10.2018
// Time: 14:23

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;

public class RequestProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        String path = exchange.getIn().getHeader("CamelHttpPath", String.class);
        exchange.setPattern(ExchangePattern.InOnly);
        exchange.getIn().setBody(path, String.class);
    }
}
