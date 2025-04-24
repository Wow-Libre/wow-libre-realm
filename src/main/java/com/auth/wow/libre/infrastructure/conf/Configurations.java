package com.auth.wow.libre.infrastructure.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class Configurations {

    @Value("${application.urls.wow-libre-login}")
    private String pathLoginWowLibre;
    @Value("${application.urls.wow-libre-server}")
    private String pathServerWowLibre;

    @Value("${application.account.username}")
    private String loginUsername;
    @Value("${application.account.password}")
    private String loginPassword;
    @Value("${application.account.api-key}")
    private String serverApiKey;

    @Value("${google.api-secret}")
    private String googleApiSecret;
    @Value("${server-web.emulator-type}")
    private String emulatorType;
}
