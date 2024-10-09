package com.auth.wow.libre.infrastructure.entities.characters;

import com.auth.wow.libre.infrastructure.entities.characters.dto.*;
import jakarta.persistence.*;
import lombok.*;

@IdClass(CharacterSkillsId.class)
@Data
@Entity
@Table(name = "character_skills")
public class CharacterSkillsEntity {
    @Id
    @Column(name = "guid")
    private Long characterId;
    @Id
    private Long skill;
    private Long value;
    private Long max;
}
