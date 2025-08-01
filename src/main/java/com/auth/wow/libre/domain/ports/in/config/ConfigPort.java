package com.auth.wow.libre.domain.ports.in.config;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.enums.*;

public interface ConfigPort {
    void create(String username, String password, String apiKey, String emulator, Integer expansionId, byte[] salt,
                String transactionId);

    GameMasterCredentials credentials(String transactionId);

    String apiKey(String transactionId);

    String apiSecret(String apiKey, String transactionId);

    EmulatorCore emulator(String transactionId);
}
