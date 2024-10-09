package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class UpdateAccountDto {

    @NotNull
    private String password;
    @NotNull
    private Long accountId;
    @NotNull
    private byte[] salt;
}
