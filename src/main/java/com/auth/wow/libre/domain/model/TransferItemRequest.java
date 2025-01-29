package com.auth.wow.libre.domain.model;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class TransferItemRequest {
    @NotNull
    private Long characterId;
    @NotNull
    private Long friendId;
    @NotNull
    private Long itemId;
    @NotNull
    private Integer quantity;
    @NotNull
    private Long accountId;
}
