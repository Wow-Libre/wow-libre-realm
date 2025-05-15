package com.auth.wow.libre.infrastructure.repositories.characters.character_queststatus;

import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.transaction.annotation.*;


public interface CharacterQuestStatusRepository extends CrudRepository<CharacterQuestStatusEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM CharacterQuestStatusEntity c WHERE c.id = :id")
    void deleteAllById(Long id);
}
