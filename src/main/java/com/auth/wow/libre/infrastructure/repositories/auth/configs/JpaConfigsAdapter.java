package com.auth.wow.libre.infrastructure.repositories.auth.configs;

import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.out.configs.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import org.springframework.stereotype.*;

@Repository
public class JpaConfigsAdapter implements ObtainConfigs, SaveConfigs {
    private final ConfigsRepository configsRepository;

    public JpaConfigsAdapter(ConfigsRepository configsRepository) {
        this.configsRepository = configsRepository;
    }


    @Override
    public ConfigsEntity getActiveConfigs(String transactionId) {
        return configsRepository.findByStatusIsTrue().orElseThrow(
                () -> new InternalException("No active configs found", transactionId));
    }

    @Override
    public void save(ConfigsEntity configs) {
        configsRepository.save(configs);
    }
}
