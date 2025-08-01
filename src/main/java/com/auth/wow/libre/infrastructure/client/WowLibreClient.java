package com.auth.wow.libre.infrastructure.client;

import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.model.shared.*;
import com.auth.wow.libre.infrastructure.conf.*;
import org.slf4j.*;
import org.springframework.core.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;

import java.util.*;

import static com.auth.wow.libre.domain.model.constant.Constants.*;

@Component
public class WowLibreClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(WowLibreClient.class);

    private final RestTemplate restTemplate;
    private final Configurations configurations;

    public WowLibreClient(RestTemplate restTemplate, Configurations configurations) {
        this.restTemplate = restTemplate;
        this.configurations = configurations;
    }

    public String getApiSecret(String apiKey, String transactionId) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HEADER_TRANSACTION_ID, transactionId);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<GenericResponse<String>> response = restTemplate.exchange(String.format("%s/core/api" +
                                    "/realm/key?api_key=%s",
                            configurations.getHostGetKeyCore(), apiKey),
                    HttpMethod.GET, entity,
                    new ParameterizedTypeReference<>() {
                    });

            if (response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                GenericResponse<String> body = Objects.requireNonNull(response.getBody());
                return body.getData();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("[WowLibreClient] [getApiSecret] Client/Server Error: {}. The request failed with a client " +
                            "or " +
                            "server error. " +
                            "HTTP Status: {}, Response Body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InternalException(Objects.requireNonNull(e.getResponseBodyAs(GenericResponse.class)).getMessage(), transactionId);
        } catch (Exception e) {
            LOGGER.error("[WowLibreClient] [getApiSecret] Unexpected Error: {}. An unexpected error occurred during " +
                            "the" +
                            " transaction with ID: {}.",
                    e.getMessage(), transactionId, e);
            throw new InternalException("Unexpected transaction failure", transactionId);
        }

        throw new InternalException("Unexpected transaction failure", transactionId);

    }

}
