package com.auth.wow.libre.infrastructure.entities;

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
    @Column(name = "last_ip")
    private String lastIp;
    @Column(name = "email")
    private String email;
    @Column(name = "totp_secret")
    private String otpSecret;
    private boolean locked;
    private boolean online;
    private String expansion;
    @Column(name = "failed_logins")
    private String failedLogins;


    @JoinColumn(
            name = "account_web",
            referencedColumnName = "id")
    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER)
    private AccountWebEntity accountWeb;

    @PrePersist
    public void prePersist() {
        this.joinDate = LocalDate.now();
        this.expansion = "2";
        this.failedLogins = "0";
        this.lastIp = "";
    }


    public AccountEntity() {
    }

}
