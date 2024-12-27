package com.auth.wow.libre.domain.ports.out.guild;

import com.auth.wow.libre.infrastructure.entities.characters.*;

import java.util.*;

public interface ObtainGuild {

    List<GuildEntity> getGuilds(Integer size, Integer page, String search, String transactionId);

    Optional<GuildEntity> getGuild(Long guildId);

    Long getGuildCount(String transactionId);

    Long count(String transactionId);
}
