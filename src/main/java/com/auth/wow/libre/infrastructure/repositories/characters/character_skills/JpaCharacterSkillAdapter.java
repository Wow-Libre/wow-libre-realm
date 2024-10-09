package com.auth.wow.libre.infrastructure.repositories.characters.character_skills;

import com.auth.wow.libre.domain.ports.out.character_skills.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaCharacterSkillAdapter implements ObtainCharacterSkills {
    private final CharacterSkillsRepository characterSkillsRepository;

    public JpaCharacterSkillAdapter(CharacterSkillsRepository characterSkillsRepository) {
        this.characterSkillsRepository = characterSkillsRepository;
    }

    @Override
    public List<CharacterSkillsEntity> getCharacterIdSkills(Long characterId) {
        return characterSkillsRepository.findByCharacterId(characterId);
    }
}
