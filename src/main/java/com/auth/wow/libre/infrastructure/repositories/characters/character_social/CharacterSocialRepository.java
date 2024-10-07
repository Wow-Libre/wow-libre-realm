package com.auth.wow.libre.infrastructure.repositories.characters.character_social;

import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.*;

import java.util.*;

public interface CharacterSocialRepository extends CrudRepository<CharacterSocialEntity, Long> {
    @Query("SELECT  ch FROM CharacterSocialEntity ch WHERE ch.guid = :guid")
    List<CharacterSocialEntity> findByGuid(@Param("guid") Long guid);
}
