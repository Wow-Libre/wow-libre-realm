package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.*;

@Data
public class BenefitsGuildDto {
    @NotNull
    private Long userId;
    @NotNull
    private Long accountId;
    @NotNull
    private Long characterId;

    private List<ItemQuantityDto> items;
}
