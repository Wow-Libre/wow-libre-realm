package com.auth.wow.libre.domain.model;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public class Account {
  public final Long id;
  public final String username;
  public final byte[] salt;
  public final byte[] verifier;
  public final String country;
  public final LocalDate dateOfBirth;
  public final String firstName;
  public final String lastName;
  public final String cellPhone;
  public final String email;
  public final String password;
  public final Long accountWebId;
  public final Boolean locked;
  public final String otpSecret;
}
