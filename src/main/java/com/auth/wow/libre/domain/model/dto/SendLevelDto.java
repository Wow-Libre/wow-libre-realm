package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class SendLevelDto {
    @NotNull
    private Long characterId;
    @NotNull
    private Long friendId;
    @NotNull
    private Long accountId;
    @NotNull
    private Long userId;
    @NotNull
    private Integer level;
    @NotNull
    private Double cost;
}
