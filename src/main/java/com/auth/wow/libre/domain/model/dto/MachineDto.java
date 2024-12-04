package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class MachineDto {
    @NotNull
    private String type;
    @NotNull
    private Long accountId;
    @NotNull
    private Long characterId;
}
