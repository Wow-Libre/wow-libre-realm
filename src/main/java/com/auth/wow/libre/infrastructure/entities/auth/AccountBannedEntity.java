package com.auth.wow.libre.infrastructure.entities.auth;

import jakarta.persistence.*;
import lombok.*;

import java.io.*;

@Data
@Entity
@Table(name = "account_banned")
public class AccountBannedEntity implements Serializable {
    @Id
    @Column(name = "id")
    private Long accountId;
    private Long bandate;
    private Long unbandate;
    private String bannedby;
    private String banreason;
    private boolean active;

}
