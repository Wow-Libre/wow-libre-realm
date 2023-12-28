package com.auth.wow.libre.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public class AccountDetail {

  private String username;
  private byte[] salt;
  private byte[] verifier;
  private String country;
  @JsonProperty("date_of_birth")
  private LocalDate dateOfBirth;
  @JsonProperty("first_name")
  private String firstName;
  @JsonProperty("last_name")
  private String lastName;
  @JsonProperty("cell_phone")
  private String cellPhone;
  private String email;
  private String password;
  @JsonProperty("account_web_id")
  private Long accountWebId;

}
