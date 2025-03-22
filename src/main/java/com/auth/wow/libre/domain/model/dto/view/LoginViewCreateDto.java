package com.auth.wow.libre.domain.model.dto.view;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.*;

@Data
public class LoginViewCreateDto {
    @NotNull
    @Length(min = 5, max = 20)
    private String username;
    @NotNull
    @Length(min = 5, max = 20)
    private String password;
}
