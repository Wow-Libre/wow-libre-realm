package com.auth.wow.libre.infrastructure.repositories.characters.guild;

import com.auth.wow.libre.infrastructure.entities.characters.*;
import jakarta.validation.constraints.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface GuildRepository extends CrudRepository<GuildEntity, Long> {

    List<GuildEntity> findAll(Pageable pageable);

    Optional<GuildEntity> findById(@NotNull Long id);

    @Query("SELECT COUNT(g) FROM GuildEntity g")
    long countAllGuilds();

    @Query("SELECT g FROM GuildEntity g WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<GuildEntity> findByNameContainingIgnoreCase(String name);
}
