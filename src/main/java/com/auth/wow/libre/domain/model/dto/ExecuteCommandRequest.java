package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class ExecuteCommandRequest {
    @NotNull
    private String message;
}
