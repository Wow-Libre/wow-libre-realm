package com.auth.wow.libre.infrastructure.client;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.ports.in.config.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.ws.transport.http.*;

import java.io.*;
import java.net.*;
import java.util.*;

@Configuration
public class WebServiceMessageSenderWithAuth extends HttpUrlConnectionMessageSender {

    private final ConfigPort configPort;

    public WebServiceMessageSenderWithAuth(ConfigPort configPort) {
        this.configPort = configPort;
    }

    @Override
    protected void prepareConnection(HttpURLConnection connection)
            throws IOException {

        GameMasterCredentials gameMasterCredentials = configPort.credentials("");

        Base64.Encoder enc = Base64.getEncoder();
        String credentials = String.format("%s:%s", gameMasterCredentials.username(), gameMasterCredentials.password());
        String encodedAuthorization = enc.encodeToString(credentials.getBytes());
        connection.setRequestProperty(HttpHeaders.AUTHORIZATION, "Basic " + encodedAuthorization);

        super.prepareConnection(connection);
    }
}
