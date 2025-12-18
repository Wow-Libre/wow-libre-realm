package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class UpdateStatsRequest {
    @NotNull
    private String type;
    @NotNull
    private String reference;
    @NotNull
    private Long userId;
    @NotNull
    private Long accountId;
    @NotNull
    private Long characterId;
}
