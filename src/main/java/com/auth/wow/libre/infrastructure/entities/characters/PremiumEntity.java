package com.auth.wow.libre.infrastructure.entities.characters;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "premium")
public class PremiumEntity {
    @Id
    @Column(name = "accountid")
    private Long accountId;
    private boolean active;
}
