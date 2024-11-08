package com.auth.wow.libre.domain.ports.out.guild;

import com.auth.wow.libre.infrastructure.entities.characters.*;

public interface SaveGuild {
    void save(GuildEntity guildEntity, String transactionId);
}
