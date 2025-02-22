package com.auth.wow.libre.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@AllArgsConstructor
@Getter
public class VerifyCaptchaRequest {
    private String response;
    @JsonProperty("remoteip")
    private String remoteip;
}
