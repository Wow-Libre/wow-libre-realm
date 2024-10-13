package com.auth.wow.libre.infrastructure.repositories.characters.guild_member;


import com.auth.wow.libre.domain.ports.out.guild_members.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaGuildMemberAdapter implements ObtainGuildMember, SaveGuildMember {

    private final GuildMemberRepository guildMemberRepository;

    public JpaGuildMemberAdapter(GuildMemberRepository guildMemberRepository) {
        this.guildMemberRepository = guildMemberRepository;
    }

    @Override
    public long numberMembers(Long guildId) {
        return guildMemberRepository.countById(guildId);
    }

    @Override
    public Optional<GuildMemberEntity> guildMemberByCharacterId(Long characterId, String transactionId) {
        return guildMemberRepository.findByGuid(characterId);
    }

    @Override
    public void save(GuildMemberEntity guildMember) {
        guildMemberRepository.save(guildMember);
    }
}
