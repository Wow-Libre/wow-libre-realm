package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class ChangePasswordAccountDto {
    @NotNull
    private String password;
    @NotNull
    private Long accountId;
    @NotNull
    private Long userId;
    @NotNull
    private byte[] salt;
    @NotNull
    private Integer expansionId;
}
