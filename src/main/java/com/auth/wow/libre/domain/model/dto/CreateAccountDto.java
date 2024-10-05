package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.*;

@Data
public class CreateAccountDto {
    @NotNull
    @Length(min = 5, max = 20)
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String email;
    private boolean rebuildUsername;
    @NotNull
    private Long userId;
    @NotNull
    @Length(min = 1, max = 10)
    private String expansion;
    @NotNull
    private byte[] salt;
}
