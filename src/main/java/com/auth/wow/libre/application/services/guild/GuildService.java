package com.auth.wow.libre.application.services.guild;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.characters.*;
import com.auth.wow.libre.domain.ports.in.comands.*;
import com.auth.wow.libre.domain.ports.in.guild.*;
import com.auth.wow.libre.domain.ports.in.guild_member.*;
import com.auth.wow.libre.domain.ports.out.guild.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import jakarta.xml.bind.*;
import org.slf4j.*;
import org.springframework.stereotype.*;
import org.springframework.ws.soap.client.*;

import java.time.*;
import java.util.*;

@Service
public class GuildService implements GuildPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuildService.class);

    private static final int GOLD_VALUE = 10000;
    private static final int SILVER_VALUE = 100;
    private final ObtainGuild obtainGuild;
    private final GuildMemberPort guildMemberPort;
    private final CharactersPort charactersPort;
    private final ExecuteCommandsPort executeCommandsPort;

    public GuildService(ObtainGuild obtainGuild, GuildMemberPort guildMemberPort, CharactersPort charactersPort,
                        ExecuteCommandsPort executeCommandsPort) {
        this.obtainGuild = obtainGuild;
        this.guildMemberPort = guildMemberPort;
        this.charactersPort = charactersPort;
        this.executeCommandsPort = executeCommandsPort;
    }


    @Override
    public GuildsDto findAll(Integer size, Integer page, String search, String transactionId) {
        GuildsDto guilds = new GuildsDto();
        List<GuildEntity> listGuilds = obtainGuild.getGuilds(size, page, search, transactionId);

        guilds.setGuilds(listGuilds.stream().map(this::mapToModel).toList());
        guilds.setSize(obtainGuild.getGuildCount(transactionId));
        return guilds;
    }

    @Override
    public GuildDto detail(Long guildId, String transactionId) {

        Optional<GuildModel> getGuild = obtainGuild.getGuild(guildId).map(this::mapToModel);

        if (getGuild.isEmpty()) {
            throw new NotFoundException("The requested guild does not exist", transactionId);
        }

        final GuildModel guild = getGuild.get();

        return new GuildDto(guild.id, guild.name, guild.leaderName, guild.emblemStyle, guild.emblemColor,
                guild.borderStyle, guild.borderColor, guild.info, guild.motd, guild.createDate, guild.bankMoney,
                guild.members, null,
                guild.publicAccess, calculateMoneyString(guild.bankMoney));
    }

    @Override
    public void attach(Long guildId, Long accountId, Long characterId, String transactionId) {

        Optional<GuildModel> getGuild = obtainGuild.getGuild(guildId).map(this::mapToModel);

        if (getGuild.isEmpty()) {
            throw new NotFoundException("The requested guild does not exist", transactionId);
        }

        GuildModel guild = getGuild.get();

        if (!guild.publicAccess) {
            throw new InternalException("The brotherhood is currently not public", transactionId);
        }

        CharacterDetailDto character = charactersPort.getCharacter(characterId, accountId, transactionId);

        if (character == null) {
            throw new InternalException("The requested characters does not exist", transactionId);
        }

        final String message = String.format("%s Te da la bienvenida.", guild.name);
        final String body = guild.motd;

        try {

            executeCommandsPort.execute(CommandsCore.invite(character.getName(), getGuild.get().name), transactionId);
            executeCommandsPort.execute(CommandsCore.sendMail(character.getName(), message, body), transactionId);
        } catch (SoapFaultClientException | JAXBException e) {
            LOGGER.error("It was not possible to link the client to the brotherhood. TransactionId [{}] - " +
                            "LocalizedMessage [{}] - Message [{}]",
                    transactionId, e.getLocalizedMessage(), e.getMessage());
            throw new InternalException("The request to join the brotherhood could not be made, please check if " +
                    "you  already belong to it", transactionId);
        }
    }

    @Override
    public void unInviteGuild(Long accountId, Long characterId, String transactionId) {


        CharacterDetailDto character = charactersPort.getCharacter(characterId, accountId, transactionId);

        if (character == null) {
            throw new InternalException("The requested characters does not exist", transactionId);
        }

        GuildMemberModel guildMember =
                guildMemberPort.guildMemberByCharacterId(character.id, transactionId);

        if (guildMember == null) {
            throw new NotFoundException("The requested guild does not exist", transactionId);
        }

        if (guildMember.rank() == 0) {
            throw new InternalException("You cannot leave the guild without leaving a guild master.", transactionId);
        }

        try {

            executeCommandsPort.execute(CommandsCore.unInvite(character.getName()), transactionId);
        } catch (SoapFaultClientException | JAXBException e) {
            LOGGER.error("It was not possible to link the client to the brotherhood. TransactionId [{}] - " +
                            "LocalizedMessage [{}] - Message [{}]",
                    transactionId, e.getLocalizedMessage(), e.getMessage());
            throw new InternalException("The request to join the brotherhood could not be made, please check if " +
                    "you  already belong to it", transactionId);
        }

    }

    @Override
    public GuildDto detail(Long accountId, Long characterId, String transactionId) {

        GuildMemberModel guildMember = guildMemberPort.guildMemberByCharacterId(characterId, transactionId);

        if (guildMember == null) {
            throw new NotFoundException("The requested guild does not exist", transactionId);
        }

        Optional<GuildModel> getGuild = obtainGuild.getGuild(guildMember.guildId()).map(this::mapToModel);

        if (getGuild.isEmpty()) {
            throw new NotFoundException("The requested guild does not exist", transactionId);
        }

        GuildModel guild = getGuild.get();

        return new GuildDto(guild.id, guild.name, guild.leaderName, guild.emblemStyle, guild.emblemColor,
                guild.borderStyle, guild.borderColor, guild.info, guild.motd, guild.createDate, guild.bankMoney,
                guild.members, null,
                guild.publicAccess, calculateMoneyString(guild.bankMoney));
    }

    private GuildModel mapToModel(GuildEntity guildEntity) {
        Date dateCreate = Date.from(Instant.ofEpochMilli(guildEntity.getCreateDate() * 1000));
        long members = guildMemberPort.accountMemberGuildId(guildEntity.getId());
        CharacterDetailDto character = charactersPort.getCharacter(Long.valueOf(guildEntity.getLeaderGuid()), "");

        return new GuildModel(guildEntity.getId(), guildEntity.getName(), character.getName(),
                guildEntity.getEmblemStyle(), guildEntity.getEmblemColor(), guildEntity.getBorderStyle(),
                guildEntity.getBorderColor(), guildEntity.getInfo(), guildEntity.getMotd(),
                dateCreate, guildEntity.getBankMoney(), members, guildEntity.getPublicAccess());
    }

    public static String calculateMoneyString(long money) {
        long gold = money / GOLD_VALUE;
        long remainingSilver = money % GOLD_VALUE;
        long silver = remainingSilver / SILVER_VALUE;

        String goldString = formatGold(gold);
        return goldString + " " + silver + "s ";
    }

    private static String formatGold(long gold) {
        if (gold >= 1_000_000) {
            long millionGold = gold / 1_000_000;
            return millionGold + "M " + (gold % 1_000_000) / 1_000 + "K " + gold % 1_000 + "g";
        } else if (gold >= 1_000) {
            long thousandGold = gold / 1_000;
            return thousandGold + "K " + gold % 1_000 + "g";
        } else {
            return gold + "g";
        }
    }
}
