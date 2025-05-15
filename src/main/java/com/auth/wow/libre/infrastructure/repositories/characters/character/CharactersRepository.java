package com.auth.wow.libre.infrastructure.repositories.characters.character;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
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
    List<CharactersEntity> findByCharacterAvailableMoney(@Param("money") Double money,
                                                         @Param("accountId") Long accountId);


    @Query("SELECT new com.auth.wow.libre.domain.model.FactionsDto(" +
            "COUNT(a), " +
            "SUM(CASE WHEN a.race IN (1, 3, 4, 7, 11) THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN a.race IN (2, 5, 6, 8, 10) THEN 1 ELSE 0 END)) " +
            "FROM CharactersEntity a")
    FactionsDto characterCountFactions();

    @Query("SELECT new com.auth.wow.libre.domain.model.dto.LevelRangeDTO(" +
            "CASE " +
            "   WHEN c.level BETWEEN 1 AND 10 THEN '1-10' " +
            "   WHEN c.level BETWEEN 11 AND 20 THEN '11-20' " +
            "   WHEN c.level BETWEEN 21 AND 30 THEN '21-30' " +
            "   WHEN c.level BETWEEN 31 AND 40 THEN '31-40' " +
            "   WHEN c.level BETWEEN 41 AND 50 THEN '41-50' " +
            "   WHEN c.level BETWEEN 51 AND 60 THEN '51-60' " +
            "   WHEN c.level BETWEEN 61 AND 70 THEN '61-70' " +
            "   WHEN c.level BETWEEN 71 AND 80 THEN '71-80' " +
            "   ELSE 'Other' " +
            "END, COUNT(c)) " +
            "FROM CharactersEntity c " +
            "GROUP BY " +
            "CASE " +
            "   WHEN c.level BETWEEN 1 AND 10 THEN '1-10' " +
            "   WHEN c.level BETWEEN 11 AND 20 THEN '11-20' " +
            "   WHEN c.level BETWEEN 21 AND 30 THEN '21-30' " +
            "   WHEN c.level BETWEEN 31 AND 40 THEN '31-40' " +
            "   WHEN c.level BETWEEN 41 AND 50 THEN '41-50' " +
            "   WHEN c.level BETWEEN 51 AND 60 THEN '51-60' " +
            "   WHEN c.level BETWEEN 61 AND 70 THEN '61-70' " +
            "   WHEN c.level BETWEEN 71 AND 80 THEN '71-80' " +
            "   ELSE 'Other' " +
            "END " +
            "ORDER BY 1")
    List<LevelRangeDTO> findUserCountsByLevelRange();


    @Query("SELECT c FROM CharactersEntity c WHERE c.level >= :level AND c.online=1")
    List<CharactersEntity> findByCharacterOnlineByLevel(@Param("level") Integer level);


}
