package com.auth.wow.libre.infrastructure.entities.characters;

import com.auth.wow.libre.infrastructure.entities.characters.dto.*;
import jakarta.persistence.*;
import lombok.*;

import java.io.*;

@IdClass(ItemInstanceId.class)
@Data
@Entity
@Table(name = "item_instance")
public class ItemInstanceEntity implements Serializable {

    @Id
    private Long guid;
    @Column(name = "item_entry")
    private Long itemEntry;
    @Column(name = "owner_guid")
    private Long ownerGuid;
    private Long count;
}
