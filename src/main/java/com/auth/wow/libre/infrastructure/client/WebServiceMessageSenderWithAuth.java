package com.auth.wow.libre.infrastructure.client;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.ws.transport.http.*;

import java.io.*;
import java.net.*;
import java.util.*;

@Configuration
public class WebServiceMessageSenderWithAuth extends HttpUrlConnectionMessageSender {

    @Value("${application.credentials.command.username}")
    private String username;
    @Value("${application.credentials.command.password}")
    private String password;

    @Override
    protected void prepareConnection(HttpURLConnection connection)
            throws IOException {

        Base64.Encoder enc = Base64.getEncoder();
        String credentials = String.format("%s:%s", username, password);
        String encodedAuthorization = enc.encodeToString(credentials.getBytes());
        connection.setRequestProperty(HttpHeaders.AUTHORIZATION, "Basic " + encodedAuthorization);

        super.prepareConnection(connection);
    }
}
