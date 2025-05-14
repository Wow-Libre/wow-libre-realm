package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class TeleportDto {
    @NotNull
    private Double positionX;
    @NotNull
    private Double positionY;
    @NotNull
    private Double positionZ;
    @NotNull
    private Integer map;
    @NotNull
    private Integer orientation;
    @NotNull
    private Double zone;
    @NotNull
    private Long characterId;
    @NotNull
    private Long accountId;
}
