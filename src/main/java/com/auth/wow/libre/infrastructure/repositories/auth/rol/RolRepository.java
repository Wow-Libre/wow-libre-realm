package com.auth.wow.libre.infrastructure.repositories.auth.rol;

import com.auth.wow.libre.infrastructure.entities.auth.RolEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface RolRepository extends CrudRepository<RolEntity, Long> {
    Optional<RolEntity> findByNameAndStatusIsTrue(String name);
}
