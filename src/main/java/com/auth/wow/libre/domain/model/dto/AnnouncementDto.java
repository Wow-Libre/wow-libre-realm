package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class AnnouncementDto {
    @NotNull
    private Long accountId;
    @NotNull
    private Long userId;
    @NotNull
    private Long characterId;
    @NotNull
    private Long skillId;
    @NotNull
    private String message;

}
