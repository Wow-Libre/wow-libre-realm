package com.auth.wow.libre.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    public final boolean status;
    public final boolean verified;
    public final String avatarUrl;
    public final String language;
    public final String otp;

    public final Integer recoveryRequests;
    public final LocalDateTime dateRecovery;
    public final RolModel rol;
    public final String activationCode;

    public AccountWebModel(String country, LocalDate dateOfBirth, String firstName, String lastName,
                           String cellPhone, String email, String password, boolean status, boolean verified,
                           String avatarUrl, String language, String otp, Integer recoveryRequests,
                           LocalDateTime dateRecovery, RolModel rol, String activationCode) {
        this.otp = otp;
        this.recoveryRequests = recoveryRequests;
        this.dateRecovery = dateRecovery;
        this.activationCode = activationCode;
        this.id = null;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellPhone = cellPhone;
        this.email = email;
        this.password = password;
        this.status = status;
        this.verified = verified;
        this.avatarUrl = avatarUrl;
        this.language = language;
        this.rol = rol;
    }

    public AccountWebModel(Long id, String country, LocalDate dateOfBirth, String firstName, String lastName,
                           String cellPhone, String email, String password, Long rolId, String rolName,
                           boolean status, boolean verified, String avatarUrl, String language, String otp,
                           Integer recoveryRequests, LocalDateTime dateRecovery, RolModel rol, String activationCode) {
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
        this.status = status;
        this.verified = verified;
        this.avatarUrl = avatarUrl;
        this.language = language;
        this.otp = otp;
        this.recoveryRequests = recoveryRequests;
        this.dateRecovery = dateRecovery;
        this.rol = rol;
        this.activationCode = activationCode;
    }
}
