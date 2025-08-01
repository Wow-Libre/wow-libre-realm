package com.auth.wow.libre.domain.ports.out.configs;

import com.auth.wow.libre.infrastructure.entities.auth.*;

public interface ObtainConfigs {
    ConfigsEntity getActiveConfigs(String transactionId);
}
