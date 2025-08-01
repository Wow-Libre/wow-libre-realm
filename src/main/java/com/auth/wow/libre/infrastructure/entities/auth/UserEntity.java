package com.auth.wow.libre.infrastructure.entities.auth;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String username;
    private String password;
    private String rol;
    private boolean status;
}
