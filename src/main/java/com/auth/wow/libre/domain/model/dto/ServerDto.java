package com.auth.wow.libre.domain.model.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
@NoArgsConstructor
public class ServerDto {
    private String name;
    @JsonProperty("api_key")
    private String apiKey;
    @JsonProperty("api_secret")
    private String apiSecret;
    private byte[] salt;
}
