package com.auth.wow.libre.infrastructure.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class Configurations {
    @Value("${application.urls.additional.confirm-account}")
    private String baseUrlConfirmationAccount;
}
