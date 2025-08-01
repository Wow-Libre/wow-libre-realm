package com.auth.wow.libre.application.services.wow_libre;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.wow_libre.*;
import com.auth.wow.libre.infrastructure.client.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

@Service
public class WowLibreService implements WowLibrePort {
    private static final Logger LOGGER = LoggerFactory.getLogger(WowLibreService.class);

    private final WowLibreClient wowLibreClient;

    public WowLibreService(WowLibreClient wowLibreClient) {
        this.wowLibreClient = wowLibreClient;
    }

    @Override
    public ServerKey getApiSecret(String apiKey, String transactionId) {
        String apiSecret = wowLibreClient.getApiSecret(apiKey, transactionId);

        if (apiSecret == null) {
            LOGGER.error("[WowLibreService][apiSecret] An error occurred when obtaining the secret api with the free " +
                    "wow central " +
                    "base {}", transactionId);
            throw new InternalException("An error occurred when obtaining the secret api with the free wow central " +
                    "base", transactionId);
        }

        return new ServerKey(apiSecret);
    }
}
