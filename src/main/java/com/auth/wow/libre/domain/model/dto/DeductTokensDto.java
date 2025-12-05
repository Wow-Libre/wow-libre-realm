package com.auth.wow.libre.domain.model.dto;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class DeductTokensDto {
    @NotNull
    @JsonProperty("userId")
    private Long userId;
    @NotNull
    @JsonProperty("accountId")
    private Long accountId;
    @NotNull
    @JsonProperty("characterId")
    private Long characterId;
    @NotNull
    private Long points;
}
