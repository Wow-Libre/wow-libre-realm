package com.auth.wow.libre.domain.ports.in.guild;

import com.auth.wow.libre.domain.model.dto.*;

public interface GuildPort {
    GuildsDto findAll(Integer size, Integer page, String search, String transactionId);

    GuildDto detail(Long guildId, String transactionId);

    void attach(Long guildId, Long accountId, Long characterId, String transactionId);

    void unInviteGuild(Long accountId, Long characterId, String transactionId);

    GuildDto detail(Long guildId, Long characterId, String transactionId);

}
