package com.auth.wow.libre.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public class Account {

  public final String username;
  public final byte[] salt;
  public final byte[] verifier;
  public final String country;
  @JsonProperty("date_of_birth")
  public final LocalDate dateOfBirth;
  @JsonProperty("first_name")
  public final String firstName;
  @JsonProperty("last_name")
  public final String lastName;
  @JsonProperty("cell_phone")
  public final String cellPhone;
  public final String email;
  public final String password;
  @JsonProperty("account_web_id")
  public final Long accountWebId;
}
