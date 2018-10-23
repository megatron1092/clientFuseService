package ru.gazprom_neft.clientfuseservice.processors;
// Created by IntelliJ IDEA.
// User: Sergey Telitsyn
// Date: 23.10.2018
// Time: 9:41

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class PathConvertorProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        String path = exchange.getIn().getHeader("CamelHttpPath", String.class).replace("/", "");
        exchange.getIn().setHeader("CamelHttpPath", path);
    }
}
