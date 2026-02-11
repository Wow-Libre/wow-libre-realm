package com.auth.wow.libre.application.services.user;

import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.realmlist.*;
import com.auth.wow.libre.domain.ports.in.user.*;
import com.auth.wow.libre.domain.ports.out.user.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import org.slf4j.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class UserService implements UserPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final ObtainUser obtainUser;
    private final SaveUser saveUser;
    private final PasswordEncoder passwordEncoder;
    private final RealmlistPort realmlistPort;

    public UserService(ObtainUser obtainUser, SaveUser saveUser,
                       PasswordEncoder passwordEncoder, RealmlistPort realmlistPort) {
        this.obtainUser = obtainUser;
        this.saveUser = saveUser;
        this.passwordEncoder = passwordEncoder;
        this.realmlistPort = realmlistPort;
    }

    @Transactional
    @Override
    public void create(String username, String password, String emulator, Long realmId,
                       Integer expansionId, String transactionId) {
        LOGGER.info("Create User By Core [Emulator]{} [ExpansionId] {} [Date]: {}", emulator, expansionId, new Date());

        Optional<UserEntity> findUsername = obtainUser.findByUsername(username);

        if (findUsername.isPresent()) {
            LOGGER.error("[UserService][create] There is already a client registered in this integration, please " +
                    "contact support");
            throw new InternalException("It is not possible to create more than one client", transactionId);
        }

        Optional<RealmlistEntity> realmList = realmlistPort.findById(realmId);

        if (realmList.isEmpty()) {
            LOGGER.error("[UserService][create] Realm with id {} not found", realmId);
            throw new InternalException("Realm not found", transactionId);
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setStatus(true);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmulator(emulator);
        user.setExpansionId(expansionId);
        user.setRealmId(realmList.get());
        user.setRol(RolType.WOW_LIBRE.getName());
        saveUser.save(user);
    }

    @Override
    public void delete(String username, String transactionId) {
        Optional<UserEntity> findUsername = obtainUser.findByUsername(username);

        if (findUsername.isEmpty()) {
            LOGGER.error("[UserService][delete] User with username {} not found", username);
            throw new InternalException("User with username not found", transactionId);
        }

        UserEntity user = findUsername.get();
        user.setStatus(false);
        saveUser.save(user);
    }


}
