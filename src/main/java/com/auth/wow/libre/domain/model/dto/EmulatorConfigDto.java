package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class EmulatorConfigDto {
    @NotNull
    @NotEmpty
    private String route;
}
