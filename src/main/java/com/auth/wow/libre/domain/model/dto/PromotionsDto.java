package com.auth.wow.libre.domain.model.dto;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.*;

@Data
public class PromotionsDto {
    @NotNull
    private Long userId;
    @NotNull
    private Long accountId;
    @NotNull
    private Long characterId;

    private List<ItemQuantityDto> items;
    @NotNull
    @NotEmpty
    private String type;
    private Double amount;
    private Integer level;
    @JsonProperty("min_lvl")
    private Integer minLvl;
    @JsonProperty("max_lvl")
    private Integer maxLvl;
}
