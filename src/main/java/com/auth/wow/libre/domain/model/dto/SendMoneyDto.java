package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class SendMoneyDto {
    @NotNull
    private Long characterId;
    @NotNull
    private Long friendId;
    @NotNull
    private Long userId;
    @NotNull
    private Long accountId;
    @NotNull
    private Long money;
    @NotNull
    private Double cost;
}
