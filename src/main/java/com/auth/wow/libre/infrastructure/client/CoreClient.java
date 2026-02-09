package com.auth.wow.libre.infrastructure.client;

import com.auth.wow.libre.infrastructure.client.soap.xml.*;
import com.auth.wow.libre.infrastructure.conf.db.RealmSoapCredentialsProvider;
import com.auth.wow.libre.infrastructure.context.RealmContext;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ws.client.core.*;
import org.springframework.ws.client.core.support.*;

@Component
public class CoreClient extends WebServiceGatewaySupport {
    private final WebServiceTemplate webServiceTemplate;
    private final RealmSoapCredentialsProvider soapCredentialsProvider;

    public CoreClient(@Qualifier("auth_azeroth_core") WebServiceTemplate webServiceTemplate,
                      RealmSoapCredentialsProvider soapCredentialsProvider) {
        this.webServiceTemplate = webServiceTemplate;
        this.soapCredentialsProvider = soapCredentialsProvider;
    }

    public void executeCommand(String command) {
        ExecuteCommand executeCommand = new ExecuteCommand();
        executeCommand.setCommand(command);
        String uri = soapCredentialsProvider.getUri(RealmContext.getCurrentRealmId());
        webServiceTemplate.marshalSendAndReceive(uri, executeCommand);
    }
}
