package com.auth.wow.libre.infrastructure.repositories.characters.guild;

import com.auth.wow.libre.domain.ports.out.guild.*;
import com.auth.wow.libre.infrastructure.entities.characters.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaGuildAdapter implements ObtainGuild, SaveGuild {

    private final GuildRepository guildRepository;

    public JpaGuildAdapter(GuildRepository guildRepository) {
        this.guildRepository = guildRepository;
    }

    @Override
    public List<GuildEntity> getGuilds(Integer size, Integer page, String search, String transactionId) {
        if (search != null && !search.isEmpty()) {
            return guildRepository.findByNameContainingIgnoreCase(search);
        }
        return guildRepository.findAll(PageRequest.of(page, size)).stream().toList();
    }

    @Override
    public Optional<GuildEntity> getGuild(Long guildId) {
        return guildRepository.findById(guildId);
    }

    @Override
    public Long getGuildCount(String transactionId) {
        return guildRepository.countAllGuilds();
    }

    @Override
    public Long count(String transactionId) {
        return guildRepository.count();
    }

    @Override
    public void save(GuildEntity guildEntity, String transactionId) {
        guildRepository.save(guildEntity);
    }
}
