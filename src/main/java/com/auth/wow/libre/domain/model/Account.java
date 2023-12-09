package com.auth.wow.libre.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class Account {

  @NotNull
  @Length(min = 5, max = 40)
  private String username;

  @NotNull
  private String salt;

  @NotNull
  private String verifier;

  @NotNull
  @Length(min = 2, max = 30)
  private String country;

  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("date_of_birth")
  private LocalDate dateOfBirth;

  @NotNull
  @Length(min = 3, max = 30)
  @JsonProperty("first_name")
  private String firstName;

  @NotNull
  @Length(min = 3, max = 30)
  @JsonProperty("last_name")
  private String lastName;

  @JsonProperty("cell_phone")
  private String cellPhone;

  @NotNull
  @Length(min = 5, max = 50)
  private String email;

  @NotNull
  private String password;

  public Account() {

  }

  public Account(String username, String country, LocalDate dateOfBirth, String firstName,
                 String lastName, String cellPhone, String email, String password) {
    this.username = username;
    this.country = country;
    this.dateOfBirth = dateOfBirth;
    this.firstName = firstName;
    this.lastName = lastName;
    this.cellPhone = cellPhone;
    this.email = email;
    this.password = password;
  }


}
