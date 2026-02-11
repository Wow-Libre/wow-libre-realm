package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.*;

@Data
@AllArgsConstructor
public class CreateAccountDto {
    @NotNull
    @Length(min = 5, max = 20)
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String email;
    @NotNull
    private Long userId;
    @NotNull
    private Integer expansionId;
}
