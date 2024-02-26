package com.project.Confguration;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

@Component
public class VirtualThreadTomcatCustomizer implements TomcatConnectorCustomizer
{
    @Override
    public void customize(Connector connector)
    {
        connector.getProtocolHandler().setExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }
}