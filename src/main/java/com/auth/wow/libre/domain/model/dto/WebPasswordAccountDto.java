package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class WebPasswordAccountDto {
  @NotNull
  public String oldPassword;
  @NotNull
  @Size(min = 5, max = 20)
  public String password;

}
