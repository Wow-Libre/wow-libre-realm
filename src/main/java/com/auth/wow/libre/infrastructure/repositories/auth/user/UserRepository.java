package com.auth.wow.libre.infrastructure.repositories.auth.user;

import com.auth.wow.libre.infrastructure.entities.auth.*;
import org.springframework.data.repository.*;

import java.util.*;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}
