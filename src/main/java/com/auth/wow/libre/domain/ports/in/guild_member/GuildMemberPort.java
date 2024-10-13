package com.auth.wow.libre.domain.ports.in.guild_member;

import com.auth.wow.libre.domain.model.*;

public interface GuildMemberPort {
    long accountMemberGuildId(Long guildId);

    void saveGuildMember(GuildMemberModel guildMemberModel, String transactionId);

    GuildMemberModel guildMemberByCharacterId(Long characterId, String transactionId);
}
