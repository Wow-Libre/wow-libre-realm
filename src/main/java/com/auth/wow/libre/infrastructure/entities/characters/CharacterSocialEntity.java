package com.auth.wow.libre.infrastructure.entities.characters;

import com.auth.wow.libre.infrastructure.entities.characters.dto.*;
import jakarta.persistence.*;
import lombok.*;

import java.io.*;

@IdClass(CharacterSocialId.class)
@Data
@Entity
@Table(name = "character_social")
public class CharacterSocialEntity implements Serializable {
    @Id
    private Long guid;
    @Id
    private Long friend;
    private Long flags;
    private String note;
}
