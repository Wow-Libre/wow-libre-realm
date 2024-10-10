package com.auth.wow.libre.infrastructure.entities.characters;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Data
@Entity
@Table(name = "character_transaction")
public class CharacterTransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "character_id")
    private Long characterId;
    @Column(name = "account_id")
    private Long accountId;
    @Column(name = "user_id")
    private Long userId;
    private Double amount;
    private String command;
    private boolean successful;
    @Column(name = "transaction_id")
    private String transactionId;
    private Boolean indebtedness;
    private Boolean status;
    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;
}
