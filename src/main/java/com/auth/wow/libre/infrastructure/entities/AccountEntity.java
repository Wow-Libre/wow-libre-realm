package com.auth.wow.libre.infrastructure.entities;

import com.auth.wow.libre.domain.model.Account;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "account")
public class AccountEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "username", unique = true)
  private String username;

  @Column(name = "salt")
  private byte[] salt;

  @Column(name = "verifier")
  private byte[] verifier;

  @Column(name = "joindate")
  private LocalDate joinDate;

  @Column(name = "email")
  private String email;
  @Column(name = "totp_secret")
  private String otpSecret;


  private boolean locked;


  @JoinColumn(
      name = "account_web",
      referencedColumnName = "id")
  @ManyToOne(
      optional = false,
      fetch = FetchType.LAZY)
  private AccountWebEntity accountWeb;

  public AccountEntity() {
  }

  public AccountEntity(String username, byte[] salt, byte[] verifier, String email, AccountWebEntity accountWeb,
                       boolean locked) {
    this.username = username;
    this.salt = salt;
    this.verifier = verifier;
    this.email = email;
    this.accountWeb = accountWeb;
    this.locked = locked;
  }


  public Account toDomainModel() {
    return Account.builder()
        .id(id)
        .username(username)
        .verifier(verifier)
        .country(accountWeb.getCountry())
        .lastName(accountWeb.getLastName())
        .dateOfBirth(accountWeb.getDateOfBirth())
        .email(email)
        .firstName(accountWeb.getFirstName())
        .cellPhone(accountWeb.getCellPhone())
        .otpSecret(otpSecret)
        .locked(locked)
        .password(accountWeb.getPassword()).accountWebId(accountWeb.getId()).build();
  }

  @PrePersist
  public void prePersist() {
    this.joinDate = LocalDate.now();
  }
}
