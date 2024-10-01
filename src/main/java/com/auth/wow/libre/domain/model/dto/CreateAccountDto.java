package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.*;

@Data
public class CreateAccountDto {
    @NotNull
    @Length(min = 5, max = 40)
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String email;
    @NotNull
    @Length(min = 40, max = 50)
    private String apiKey;
    private boolean rebuildUsername;
    @NotNull
    private Long userId;
}
