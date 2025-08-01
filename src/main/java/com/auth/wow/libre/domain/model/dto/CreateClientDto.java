package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class CreateClientDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private byte[] salt;
    @NotNull
    private String apiKey;
    @NotNull
    private String emulator;
    @NotNull
    private Integer expansionId;
    @NotNull
    private String gmUsername;
    @NotNull
    private String gmPassword;
}
