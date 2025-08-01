package com.auth.wow.libre.infrastructure.repositories.auth.user;

import com.auth.wow.libre.domain.ports.out.user.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public class JpaUserAdapter implements ObtainUser, SaveUser {
    private final UserRepository userRepository;

    public JpaUserAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void save(UserEntity user) {
        userRepository.save(user);
    }
}
