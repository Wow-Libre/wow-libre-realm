package com.auth.wow.libre.application.services.config;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.config.*;
import com.auth.wow.libre.domain.ports.in.wow_libre.*;
import com.auth.wow.libre.domain.ports.out.configs.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import com.auth.wow.libre.infrastructure.util.*;
import org.springframework.stereotype.*;

import javax.crypto.*;
import java.util.*;

@Service
public class ConfigService implements ConfigPort {
    private final ObtainConfigs obtainConfig;
    private final SaveConfigs saveConfigs;
    private final WowLibrePort wowLibrePort;

    public ConfigService(ObtainConfigs obtainConfig, SaveConfigs saveConfigs, WowLibrePort wowLibrePort) {
        this.obtainConfig = obtainConfig;
        this.saveConfigs = saveConfigs;
        this.wowLibrePort = wowLibrePort;
    }


    @Override
    public void create(String username, String password, String apiKey, String emulator, Integer expansionId,
                       byte[] salt, String transactionId) {
        ConfigsEntity saveConfig = new ConfigsEntity();
        saveConfig.setApiKey(apiKey);
        saveConfig.setEmulator(emulator);
        saveConfig.setExpansionId(expansionId);
        saveConfig.setGameMasterPassword(password);
        saveConfig.setStatus(true);
        saveConfig.setGameMasterUsername(username);
        saveConfig.setSalt(salt);
        saveConfigs.save(saveConfig);
    }

    @Override
    public String apiSecret(String apiKey, String transactionId) {
        return wowLibrePort.getApiSecret(apiKey, transactionId).keyPassword();
    }

    @Override
    public GameMasterCredentials credentials(String transactionId) {

        try {
            final ConfigsEntity configs = obtainConfig.getActiveConfigs(transactionId);
            final String apiSecret = apiSecret(configs.getApiKey(), transactionId);

            SecretKey derivedKey = KeyDerivationUtil.deriveKeyFromPassword(apiSecret, configs.getSalt());
            final String decryptPassword = EncryptionUtil.decrypt(configs.getGameMasterPassword(), derivedKey);
            return new GameMasterCredentials(configs.getGameMasterUsername(), decryptPassword);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String apiKey(String transactionId) {
        return obtainConfig.getActiveConfigs(transactionId).getApiKey();
    }

    @Override
    public EmulatorCore emulator(String transactionId) {
        final ConfigsEntity configs = obtainConfig.getActiveConfigs(transactionId);

        return Arrays.stream(EmulatorCore.values())
                .filter(c -> c.getName().equals(configs.getEmulator()))
                .findFirst()
                .orElseThrow(() -> new InternalException("Unsupported emulator core", transactionId));
    }
}
