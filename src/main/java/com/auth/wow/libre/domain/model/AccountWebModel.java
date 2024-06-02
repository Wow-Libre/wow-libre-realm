package com.auth.wow.libre.domain.model;

import java.time.LocalDate;

public class AccountWebModel {
    public Long id;
    public final String country;
    public final LocalDate dateOfBirth;
    public final String firstName;
    public final String lastName;
    public final String cellPhone;
    public final String email;
    public final String password;
    public Long rolId;
    public String rolName;

    public AccountWebModel(String country, LocalDate dateOfBirth, String firstName, String lastName,
                           String cellPhone, String email, String password) {
        this.id = null;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellPhone = cellPhone;
        this.email = email;
        this.password = password;
    }

    public AccountWebModel(Long id, String country, LocalDate dateOfBirth, String firstName, String lastName,
                           String cellPhone, String email, String password, Long rolId, String rolName) {
        this.id = id;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellPhone = cellPhone;
        this.email = email;
        this.password = password;
        this.rolId = rolId;
        this.rolName = rolName;
    }
}
