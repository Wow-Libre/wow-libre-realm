package com.auth.wow.libre.infrastructure.conf;

import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
@Data
public class Configurations {

    @Value("${application.urls.core}")
    private String hostGetKeyCore;

}
