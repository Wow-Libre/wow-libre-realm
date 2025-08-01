package com.auth.wow.libre.domain.ports.out.configs;

import com.auth.wow.libre.infrastructure.entities.auth.*;

public interface SaveConfigs {
    void save(ConfigsEntity configs);
}
