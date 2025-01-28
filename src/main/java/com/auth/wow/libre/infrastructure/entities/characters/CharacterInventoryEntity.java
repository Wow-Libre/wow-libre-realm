package com.auth.wow.libre.infrastructure.entities.characters;

import jakarta.persistence.*;
import lombok.*;

import java.io.*;

@Data
@Entity
@Table(name = "character_inventory")
public class CharacterInventoryEntity implements Serializable {
    @Id
    private Long guid;
    private Long bag;
    private Long slot;
    private Long item;
}
