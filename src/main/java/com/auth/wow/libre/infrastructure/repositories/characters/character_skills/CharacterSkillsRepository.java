package com.auth.wow.libre.infrastructure.repositories.characters.character_skills;

import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface  CharacterSkillsRepository extends CrudRepository<CharacterSkillsEntity, Long> {
    List<CharacterSkillsEntity> findByCharacterId(Long characterId);
}
