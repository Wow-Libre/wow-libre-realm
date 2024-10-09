package com.auth.wow.libre.domain.ports.out.character_skills;

import com.auth.wow.libre.infrastructure.entities.characters.*;

import java.util.*;

public interface  ObtainCharacterSkills {
    List<CharacterSkillsEntity> getCharacterIdSkills(Long characterId);

}
