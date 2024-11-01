package com.auth.wow.libre.infrastructure.client;

import com.auth.wow.libre.infrastructure.client.soap.xml.*;
import org.springframework.stereotype.*;
import org.springframework.ws.client.core.*;
import org.springframework.ws.client.core.support.*;

@Component
public class CoreClient extends WebServiceGatewaySupport {
    private final WebServiceTemplate webServiceTemplate;

    public CoreClient(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public void executeCommand(String command) {
        ExecuteCommand executeCommand = new ExecuteCommand();
        executeCommand.setCommand(command);
        webServiceTemplate.marshalSendAndReceive(executeCommand);
    }
}
