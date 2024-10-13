package com.auth.wow.libre.application.services.guild_member;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.ports.in.guild_member.*;
import com.auth.wow.libre.domain.ports.out.guild_members.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.stereotype.*;

@Service
public class GuildMemberService implements GuildMemberPort {
    private final ObtainGuildMember obtainGuildMember;
    private final SaveGuildMember saveGuildMember;

    public GuildMemberService(ObtainGuildMember obtainGuildMember, SaveGuildMember saveGuildMember) {
        this.obtainGuildMember = obtainGuildMember;
        this.saveGuildMember = saveGuildMember;
    }

    @Override
    public long accountMemberGuildId(Long guildId) {
        return obtainGuildMember.numberMembers(guildId);
    }

    @Override
    public void saveGuildMember(GuildMemberModel guildMemberModel, String transactionId) {
        saveGuildMember.save(toFromModel(guildMemberModel));
    }

    @Override
    public GuildMemberModel guildMemberByCharacterId(Long characterId, String transactionId) {
        return obtainGuildMember.guildMemberByCharacterId(characterId, transactionId)
                .map(this::mapToModel).orElse(null);
    }

    private GuildMemberModel mapToModel(GuildMemberEntity guildMember) {
        return new GuildMemberModel(guildMember.getId(), guildMember.getGuid(), guildMember.getRank());
    }

    private GuildMemberEntity toFromModel(GuildMemberModel guildMemberModel) {
        GuildMemberEntity memberEntity = new GuildMemberEntity();

        memberEntity.setId(guildMemberModel.guildId());
        memberEntity.setGuid(guildMemberModel.characterId());
        memberEntity.setRank(guildMemberModel.rank());
        memberEntity.setPnote("");
        memberEntity.setOffnote("");
        return memberEntity;
    }
}
