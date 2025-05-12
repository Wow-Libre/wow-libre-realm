package com.auth.wow.libre.infrastructure.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class Configurations {

    @Value("${application.urls.wow-libre-login}")
    private String hostLoginCore;
    @Value("${application.urls.wow-libre-server}")
    private String hostGetKeyCore;

    @Value("${application.account.username}")
    private String loginUsername;
    @Value("${application.account.password}")
    private String loginPassword;

    @Value("${application.account.api-key}")
    private String realmApiKey;
    @Value("${server-web.emulator-type}")
    private String emulatorType;
    @Value("${server-web.domain}")
    private String domain;
}
