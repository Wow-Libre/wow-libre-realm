package com.auth.wow.libre.infrastructure.repositories.auth.configs;

import com.auth.wow.libre.infrastructure.entities.auth.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface ConfigsRepository extends CrudRepository<ConfigsEntity, Long> {
    Optional<ConfigsEntity> findByStatusIsTrue();
}
