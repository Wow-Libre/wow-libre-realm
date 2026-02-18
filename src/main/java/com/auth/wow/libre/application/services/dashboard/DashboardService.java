package com.auth.wow.libre.application.services.dashboard;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.dto.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.account.*;
import com.auth.wow.libre.domain.ports.in.characters.*;
import com.auth.wow.libre.domain.ports.in.dashboard.*;
import com.auth.wow.libre.domain.ports.in.guild.*;
import com.auth.wow.libre.domain.ports.out.file.*;
import com.auth.wow.libre.infrastructure.repositories.auth.account.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;

@Service
public class DashboardService implements DashboardPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardService.class);

    private final AccountPort accountPort;
    private final CharactersPort charactersPort;
    private final GuildPort guildPort;
    private final ObtainConfigsServer obtainConfigsServer;
    private final SaveConfigsServer saveConfigsServer;


    public DashboardService(AccountPort accountPort, CharactersPort charactersPort, GuildPort guildPort,
                            ObtainConfigsServer obtainConfigsServer, SaveConfigsServer saveConfigsServer) {
        this.accountPort = accountPort;
        this.charactersPort = charactersPort;
        this.guildPort = guildPort;
        this.obtainConfigsServer = obtainConfigsServer;
        this.saveConfigsServer = saveConfigsServer;
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
        data.setRangeLevel(charactersPort.findUserCountsByLevelRange(transactionId));
        return data;
    }

    @Override
    public void updateMailAccount(String username, String updateMail, String transactionId) {
        accountPort.updateMail(username, updateMail, transactionId);
    }

    @Override
    public void bannedUser(AccountBanDto banDto, String transactionId) {
        accountPort.bannedUser(banDto.getUsername(), banDto.getDays(), banDto.getHours(), banDto.getMinutes(),
                banDto.getSeconds(),
                banDto.getBannedBy(), banDto.getBanReason(), transactionId);
    }

    @Override
    public Map<String, String> getFileConfig(String route, String transactionId) {


        final Map<String, String> configs =
                Optional.ofNullable(obtainConfigsServer.getFileConfigServer(route, transactionId))
                        .map(AuthServerConfig::valores).orElse(null);

        if (configs == null) {
            LOGGER.error("It was not possible to find and extract the file route: {} TransactionId:{}", route,
                    transactionId);
            throw new InternalException("It was not possible to find and extract the file", transactionId);
        }

        return configs;
    }

    @Override
    public void updateFileConfig(String originalFilePath, Map<String, String> replacements, String transactionId) {
        saveConfigsServer.updateConfigFile(originalFilePath, replacements);
    }

    @Override
    public EmulatorRoutesDto getEmulatorRoutes(String transactionId) {

        EmulatorRoutesDto dto = new EmulatorRoutesDto();

        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

        try {
            if (isWindows) {
                dto.setWorldServer(getWindowsProcessPath("worldserver.exe"));
                dto.setAuthServer(getWindowsProcessPath("authserver.exe"));
            } else {
                dto.setWorldServer(getLinuxProcessPath("worldserver"));
                dto.setAuthServer(getLinuxProcessPath("authserver"));
            }
        } catch (Exception e) {
            LOGGER.error("Error retrieving process paths [{}] transactionId [{}]", e.getMessage(),
                    transactionId);
        }

        return dto;

    }

    private String getWindowsProcessPath(String processName) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(
                new String[]{"cmd.exe", "/c", "wmic process where name=\"" + processName + "\" get ExecutablePath " +
                        "/value"}
        );

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        String path = null;

        while ((line = reader.readLine()) != null) {
            if (line.trim().startsWith("ExecutablePath=")) {
                path = line.replace("ExecutablePath=", "").trim();
                break;
            }
        }

        process.waitFor();
        return path;
    }

    private String getLinuxProcessPath(String processName) throws IOException, InterruptedException {
        String[] command = {
                "/bin/sh",
                "-c",
                "readlink -f /proc/$(pgrep -n " + processName + ")/exe"
        };

        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String path = reader.readLine();
        process.waitFor();

        return (path != null) ? path.trim() : null;
    }

}
