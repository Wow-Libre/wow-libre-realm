package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class AccountUpdateMailDto {
    @NotNull

    private String mail;
    @NotNull

    private String username;
}
