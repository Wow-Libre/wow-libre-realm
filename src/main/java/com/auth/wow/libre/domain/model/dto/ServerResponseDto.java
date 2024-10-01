package com.auth.wow.libre.domain.model.dto;

import lombok.*;

import java.time.*;

@Data
public class ServerResponseDto {
    private Long id;
    private String name;
    private boolean status;
    private String emulator;
    private String avatar;
    private String apiSecret;
    private String version;
    private String ip;
    private LocalDateTime creationDate;
    private String webSite;
    private String password;
    private String apiKey;
}
