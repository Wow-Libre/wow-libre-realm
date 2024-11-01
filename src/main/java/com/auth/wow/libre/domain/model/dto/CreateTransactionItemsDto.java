package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.*;

@Data
public class CreateTransactionItemsDto {
    @NotNull
    private Long userId;
    @NotNull
    private Long accountId;
    @NotNull
    private List<ItemQuantityDto> items;
    @NotNull
    @NotEmpty
    private String reference;
}
