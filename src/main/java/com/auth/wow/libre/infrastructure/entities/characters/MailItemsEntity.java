package com.auth.wow.libre.infrastructure.entities.characters;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "mail_items")
public class MailItemsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mail_id")
    private Long mailId;
    @Column(name = "item_guid")
    private Long itemId;
    @Column(name = "receiver")
    private Long characterReceiverId;
}
