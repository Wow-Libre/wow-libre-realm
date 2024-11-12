package com.auth.wow.libre.infrastructure.repositories.characters.character;

import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.*;

import java.util.*;

public interface CharactersRepository extends CrudRepository<CharactersEntity, Long> {
    List<CharactersEntity> findByAccount(Long accountId);

    List<CharactersEntity> findByAccountAndLevel(Long account, Integer level);

    Optional<CharactersEntity> findByGuidAndAccount(Long characterId, Long account);

    Optional<CharactersEntity> findByGuid(Long characterId);

    @Query("SELECT c FROM CharactersEntity c WHERE  c.online=1")
    List<CharactersEntity> findCharactersAndOnline();

    @Query("SELECT c FROM CharactersEntity c WHERE c.money >= :money AND c.account = :accountId AND c.online=0")
    List<CharactersEntity> findByCharacterAvailableMoney(@Param("money") Double money, @Param("accountId") Long accountId);

}
