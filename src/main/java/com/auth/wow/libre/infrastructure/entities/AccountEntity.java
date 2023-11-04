package com.auth.wow.libre.infrastructure.entities;

import com.auth.wow.libre.domain.model.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "account")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "salt")
    private byte[] salt;

    @Column(name = "verifier")
    private byte[] verifier;

    @Column(name = "joindate")
    private LocalDate joinDate;

    @Column(name = "country")
    private String country;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "cell_phone")
    private String cellPhone;

    @Column(name = "email")
    private String email;

    public AccountEntity() {
    }

    public AccountEntity(String username, byte[] salt, byte[] verifier, String country, LocalDate dateOfBirth,
                         String firstName, String lastName, String cellPhone, String email) {
        this.username = username;
        this.salt = salt;
        this.verifier = verifier;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellPhone = cellPhone;
        this.email = email;

    }

    public static AccountEntity fromDomainModel(Account account) {
        return new AccountEntity(account.getUsername(), account.getSalt(), account.getVerifier(),
                account.getCountry(), account.getDateOfBirth(), account.getFirstName(), account.getLastName(),
                account.getCellPhone(), account.getEmail());
    }

    public Account toDomainModel() {
        return new Account(username, country, dateOfBirth, firstName, lastName, cellPhone, email);
    }

    @PrePersist
    public void prePersist() {
        this.joinDate = LocalDate.now();
    }
}
