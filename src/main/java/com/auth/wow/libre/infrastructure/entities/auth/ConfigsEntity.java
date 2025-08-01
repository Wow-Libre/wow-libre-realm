package com.auth.wow.libre.infrastructure.entities.auth;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "configs")
public class ConfigsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "game_master_username")
    private String gameMasterUsername;
    @Column(name = "game_master_password")
    private String gameMasterPassword;
    private boolean status;
    @Column(name = "api_key")
    private String apiKey;
    private String emulator;
    @Column(name = "expansion_id")
    private Integer expansionId;
    private byte[] salt;
}
