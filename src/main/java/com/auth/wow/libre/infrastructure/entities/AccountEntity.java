package com.auth.wow.libre.infrastructure.entities;

import com.auth.wow.libre.domain.model.Account;
import com.auth.wow.libre.domain.model.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.Data;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "account")
public class AccountEntity implements Serializable {

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
    try {
      return new AccountEntity(account.getUsername(), Hex.decodeHex(account.getSalt()),
              Hex.decodeHex(account.getVerifier()), account.getEmail(), accountWeb);
    } catch (DecoderException e) {
      throw new BadRequestException("Ha ocurrido un error con el cifrado.", "");
    }
  }

  public Account toDomainModel() {
    return new Account(username, accountWeb.getCountry(), accountWeb.getDateOfBirth(), accountWeb.getFirstName(),
            accountWeb.getLastName(), accountWeb.getCellPhone(), email, accountWeb.getPassword());
  }

  @PrePersist
  public void prePersist() {
    this.joinDate = LocalDate.now();
  }
}
