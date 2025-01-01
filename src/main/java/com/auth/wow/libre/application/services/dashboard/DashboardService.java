package com.auth.wow.libre.application.services.dashboard;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.ports.in.account.*;
import com.auth.wow.libre.domain.ports.in.characters.*;
import com.auth.wow.libre.domain.ports.in.dashboard.*;
import com.auth.wow.libre.domain.ports.in.guild.*;
import com.auth.wow.libre.infrastructure.repositories.auth.account.*;
import org.springframework.stereotype.*;

@Service
public class DashboardService implements DashboardPort {
    private final AccountPort accountPort;
    private final CharactersPort charactersPort;
    private final GuildPort guildPort;

    public DashboardService(AccountPort accountPort, CharactersPort charactersPort, GuildPort guildPort) {
        this.accountPort = accountPort;
        this.charactersPort = charactersPort;
        this.guildPort = guildPort;
    }


    @Override
    public DashboardMetricsDto metricsCount(String transactionId) {
        MetricsProjection metricsAccount = accountPort.metrics(transactionId);
        FactionsDto factionsDto = charactersPort.factions(transactionId);

        DashboardMetricsDto data = new DashboardMetricsDto();
        data.setTotalUsers(metricsAccount.getTotalUsers());
        data.setOnlineUsers(metricsAccount.getOnlineUsers());
        data.setExternalRegistrations(metricsAccount.getTotalUsersExternal());
        data.setCharacterCount(factionsDto.getCharacters());
        data.setTotalGuilds(guildPort.count(transactionId));
        data.setHordas(factionsDto.getHorda());
        data.setAlianzas(factionsDto.getAlianza());

        return data;
    }
}
