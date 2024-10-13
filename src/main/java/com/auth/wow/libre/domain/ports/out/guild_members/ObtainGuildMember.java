package com.auth.wow.libre.domain.ports.out.guild_members;

import com.auth.wow.libre.infrastructure.entities.characters.*;

import java.util.*;

public interface ObtainGuildMember {
    long numberMembers(Long guildId);

    Optional<GuildMemberEntity> guildMemberByCharacterId(Long characterId, String transactionId);
}
