package com.auth.wow.libre.domain.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
@Data
public class AccountLoginDto {
  @NotNull
  @Length(min = 5, max = 50)
  private String username;
  @NotNull
  @Length(min = 5, max = 50)
  private String password;
}
