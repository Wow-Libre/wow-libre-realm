package com.auth.wow.libre.infrastructure.repositories.characters.guild_member;

import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface GuildMemberRepository extends CrudRepository<GuildMemberEntity, Long> {
    @Query("SELECT COUNT(g) FROM GuildMemberEntity g WHERE g.id = ?1")
    long countById(Long id);

    Optional<GuildMemberEntity> findByGuid(Long characterId);
}
