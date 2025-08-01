package com.auth.wow.libre.application.services.user;

import com.auth.wow.libre.domain.model.enums.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.in.config.*;
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
    private final ConfigPort configPort;


    public UserService(ObtainUser obtainUser, SaveUser saveUser,
                       PasswordEncoder passwordEncoder, ConfigPort configPort) {
        this.obtainUser = obtainUser;
        this.saveUser = saveUser;
        this.passwordEncoder = passwordEncoder;
        this.configPort = configPort;
    }

    @Transactional
    @Override
    public void create(String username, String password, byte[] salt, String emulator, String apikey,
                       Integer expansionId, String gmUsername, String gmPassword, String transactionId) {
        LOGGER.info("Create User By Core [Emulator]{} [ExpansionId] {} [Date]: {}", emulator, expansionId, new Date());

        Optional<UserEntity> findUsername = obtainUser.findByUsername(username);

        if (findUsername.isPresent()) {
            LOGGER.error("[UserService][create] There is already a client registered in this integration, please " +
                    "contact support");
            throw new InternalException("It is not possible to create more than one client", transactionId);
        }

        try {

            UserEntity user = new UserEntity();
            user.setUsername(username);
            user.setStatus(true);
            user.setPassword(passwordEncoder.encode(password));
            user.setRol(RolType.WOW_LIBRE.getName());
            saveUser.save(user);

            configPort.create(gmUsername, gmPassword, apikey, emulator, expansionId, salt, transactionId);
        } catch (Exception e) {
            LOGGER.error("[UserService][create] Could not create client {} {} {}", e.getLocalizedMessage(),
                    e.getCause(), e.getMessage());
            throw new InternalException("Could not realm config client", transactionId);
        }
    }


}
