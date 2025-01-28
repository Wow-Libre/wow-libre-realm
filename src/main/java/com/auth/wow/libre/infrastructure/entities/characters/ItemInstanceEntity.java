package com.auth.wow.libre.infrastructure.entities.characters;

import jakarta.persistence.*;
import lombok.*;

import java.io.*;

@Data
@Entity
@Table(name = "item_instance")
public class ItemInstanceEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guid")
    private Long id;
    @Column(name = "itementry")
    private Long itemEntry;
    @Column(name = "owner_guid")
    private Long ownerGuid;
    private Long count;
    private Long duration;
    private String charges;
}
