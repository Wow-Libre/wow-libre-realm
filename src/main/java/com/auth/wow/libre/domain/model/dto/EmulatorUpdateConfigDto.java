package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.*;

@Data
public class EmulatorUpdateConfigDto {
    @NotNull
    @NotEmpty
    private String route;
    @NotNull
    @NotEmpty
    private Map<String,String> configs;

}
