package com.auth.wow.libre.application.services.google;

import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.google.*;
import com.auth.wow.libre.infrastructure.client.*;
import com.auth.wow.libre.infrastructure.client.dto.*;
import org.springframework.stereotype.*;

@Service
public class GoogleService implements GooglePort {
    private final GoogleClient googleClient;

    public GoogleService(GoogleClient googleClient) {
        this.googleClient = googleClient;
    }

    @Override
    public VerifyCaptchaResponse verifyRecaptcha(String recaptchaToken, String ipAddress) {

        if (recaptchaToken == null || ipAddress == null) {
            throw new InternalException("The recaptcha fields are null so it is not possible to validate the code", "");
        }

        return googleClient.verifyRecaptcha(new VerifyCaptchaRequest(
                recaptchaToken, ipAddress));
    }
}
