package com.auth.wow.libre.infrastructure.entities.characters;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "mail")
public class MailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "messagetype")
    private Long messageType;
    @Column(name = "sender")
    private Long senderGuidId;
    @Column(name = "receiver")
    private Long receiverGuidId;
    private String subject;
    private String body;
    @Column(name = "has_items")
    private boolean hasItems;
    @Column(name = "expire_time")
    private Long expireTime;
    @Column(name = "deliver_time")
    private Long deliverTime;
    private Integer money;
}
