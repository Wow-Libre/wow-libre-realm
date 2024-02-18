package com.auth.wow.libre.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public class AccountDetail {

  public final String username;
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
  @JsonProperty("account_web_id")
  public final Long accountWebId;
  @JsonProperty("account_banned")
  public final AccountBannedDto accountBanned;
  @JsonProperty("account_muted")
  public final AccountMutedDto accountMuted;
}
