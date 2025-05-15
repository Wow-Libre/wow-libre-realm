package com.auth.wow.libre.infrastructure.repositories.characters.character_queststatus;

import com.auth.wow.libre.domain.ports.out.character_quest_status.*;
import org.springframework.stereotype.*;

@Repository
public class JpaCharacterQuestStatusAdapter implements DeleteCharacterQuest {
    private final CharacterQuestStatusRepository characterQuestStatusRepository;

    public JpaCharacterQuestStatusAdapter(CharacterQuestStatusRepository characterQuestStatusRepository) {
        this.characterQuestStatusRepository = characterQuestStatusRepository;
    }

    @Override
    public void deleteAllById(Long id) {
        characterQuestStatusRepository.deleteAllById(id);
    }
}
