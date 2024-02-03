package com.auth.wow.libre.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordAccountDto {
  @NotNull
  private String salt;
  @NotNull
  @JsonProperty("verifier")
  private String verifier;
  @NotNull
  @JsonProperty("password")
  private String password;
}
