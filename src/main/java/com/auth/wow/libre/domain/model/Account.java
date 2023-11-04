package com.auth.wow.libre.domain.model;

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
    private byte[] salt;
    @NotNull
    private byte[] verifier;
    @NotNull
    @Length(min = 5, max = 30)
    private String country;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    @NotNull
    @Length(min = 5, max = 30)
    private String firstName;
    @NotNull
    @Length(min = 5, max = 30)
    private String lastName;
    @NotNull
    @Length(min = 6, max = 20)
    private String cellPhone;
    @NotNull
    @Length(min = 5, max = 50)
    private String email;

    public Account() {

    }

    public Account(String username, String country, LocalDate dateOfBirth, String firstName, String lastName, String cellPhone, String email) {
        this.username = username;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellPhone = cellPhone;
        this.email = email;
    }
}
