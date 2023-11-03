package com.wow.libre.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "account")
public class AccountEntity {
    @Id
    private Integer id;

    private byte[] salt;

    private byte[] verifier;

    private String username;


}
