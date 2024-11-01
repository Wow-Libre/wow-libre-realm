package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class BankPaymentDto {
    @NotNull
    private Long userId;
    @NotNull
    private Double amount;
}
