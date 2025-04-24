package com.auth.wow.libre.infrastructure.client;

import com.auth.wow.libre.infrastructure.client.soap_trinity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ws.client.core.*;
import org.springframework.ws.client.core.support.*;

@Component
public class TrinityClient extends WebServiceGatewaySupport {
    private final WebServiceTemplate webServiceTemplate;

    public TrinityClient(@Qualifier("auth_trinity_core") WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public void executeCommand(String command) {
        ExecuteCommand executeCommand = new ExecuteCommand();
        executeCommand.setCommand(command);
        webServiceTemplate.marshalSendAndReceive(executeCommand);
    }
}
