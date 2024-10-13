package com.auth.wow.libre.domain.ports.out.guild_members;

import com.auth.wow.libre.infrastructure.entities.characters.*;

public interface SaveGuildMember {
    void save(GuildMemberEntity guildMember);
}
