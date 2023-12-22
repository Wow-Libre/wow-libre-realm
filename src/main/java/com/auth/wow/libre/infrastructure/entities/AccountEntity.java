package com.auth.wow.libre.infrastructure.entities;

import com.auth.wow.libre.domain.model.Account;
import jakarta.persistence.*;
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

  @JoinColumn(
          name = "account_web",
          referencedColumnName = "id")
  @ManyToOne(
          optional = false,
          fetch = FetchType.LAZY)
  private AccountWebEntity accountWeb;

  public AccountEntity() {
  }

  public AccountEntity(String username, byte[] salt, byte[] verifier, String email, AccountWebEntity accountWeb) {
    this.username = username;
    this.salt = salt;
    this.verifier = verifier;
    this.email = email;
    this.accountWeb = accountWeb;
  }

  public static AccountEntity fromDomainModel(Account account, AccountWebEntity accountWeb) {
    return new AccountEntity(account.username, account.salt,
            account.verifier, account.email, accountWeb);

  }

  public Account toDomainModel() {
    return Account.builder()
            .username(username)
            .country(accountWeb.getCountry())
            .lastName(accountWeb.getLastName())
            .dateOfBirth(accountWeb.getDateOfBirth())
            .firstName(accountWeb.getFirstName())
            .cellPhone(accountWeb.getCellPhone())
            .password(accountWeb.getPassword()).accountWebId(accountWeb.getId()).build();
  }

  @PrePersist
  public void prePersist() {
    this.joinDate = LocalDate.now();
  }
}
