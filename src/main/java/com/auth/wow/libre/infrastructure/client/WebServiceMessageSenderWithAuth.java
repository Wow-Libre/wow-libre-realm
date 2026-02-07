package com.auth.wow.libre.infrastructure.client;

import com.auth.wow.libre.domain.model.GameMasterCredentials;
import com.auth.wow.libre.infrastructure.conf.db.RealmSoapCredentialsProvider;
import com.auth.wow.libre.infrastructure.context.RealmContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class WebServiceMessageSenderWithAuth extends HttpUrlConnectionMessageSender {

    private final RealmSoapCredentialsProvider soapCredentialsProvider;

    public WebServiceMessageSenderWithAuth(RealmSoapCredentialsProvider soapCredentialsProvider) {
        this.soapCredentialsProvider = soapCredentialsProvider;
    }

    @Override
    protected void prepareConnection(HttpURLConnection connection) throws IOException {
        Long realmId = RealmContext.getCurrentRealmId();
        GameMasterCredentials gameMasterCredentials = soapCredentialsProvider.getCredentials(realmId);

        Base64.Encoder enc = Base64.getEncoder();
        String credentials = String.format("%s:%s", gameMasterCredentials.username(), gameMasterCredentials.password());
        String encodedAuthorization = enc.encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        connection.setRequestProperty(HttpHeaders.AUTHORIZATION, "Basic " + encodedAuthorization);

        super.prepareConnection(connection);
    }
}
