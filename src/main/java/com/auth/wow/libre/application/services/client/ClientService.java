package com.auth.wow.libre.application.services.client;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.model.security.*;
import com.auth.wow.libre.domain.ports.in.client.*;
import com.auth.wow.libre.domain.ports.in.jwt.*;
import com.auth.wow.libre.domain.ports.in.wow_libre.*;
import com.auth.wow.libre.domain.ports.out.client.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import com.auth.wow.libre.infrastructure.util.*;
import org.slf4j.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import javax.crypto.*;
import java.util.*;

@Service
public class ClientService implements ClientPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);
    private static final String ROL_DEFAULT = "CLIENT";
    private static final String ROL_WOW_LIBRE = "WOW_LIBRE";

    private final ObtainClient obtainClient;
    private final SaveClient saveClient;
    private final PasswordEncoder passwordEncoder;
    private final WowLibrePort wowLibrePort;
    private final JwtPort jwtPort;

    public ClientService(ObtainClient obtainClient, SaveClient saveClient,
                         PasswordEncoder passwordEncoder, WowLibrePort wowLibrePort, JwtPort jwtPort) {
        this.obtainClient = obtainClient;
        this.saveClient = saveClient;
        this.passwordEncoder = passwordEncoder;
        this.wowLibrePort = wowLibrePort;
        this.jwtPort = jwtPort;
    }

    @Override
    public void create(String username, String password, byte[] salt, String transactionId) {
        LOGGER.info("Free wow client creation request Date: {}", new Date());

        Optional<ClientEntity> findUsername = obtainClient.findByUsername(username);

        if (findUsername.isPresent()) {
            LOGGER.error("There is already a client registered in this integration, please contact support");
            throw new InternalException("It is not possible to create more than one client", transactionId);
        }

        try {
            final String jwt = wowLibrePort.login(transactionId).jwt;
            final ServerKey apiSecret = wowLibrePort.getApiSecret(jwt, transactionId);
            SecretKey derivedKey = KeyDerivationUtil.deriveKeyFromPassword(apiSecret.keyPassword(), salt);
            final String decryptPassword = EncryptionUtil.decrypt(password, derivedKey);
            final String passwordEncode = passwordEncoder.encode(decryptPassword);

            ClientEntity client = new ClientEntity();
            client.setUsername(username);
            client.setRol(ROL_WOW_LIBRE);
            client.setStatus(true);
            client.setPassword(passwordEncode);
            saveClient.save(client);
        } catch (Exception e) {
            LOGGER.error("[ClientService][create] Could not create client {} {} {}", e.getLocalizedMessage(),
                    e.getCause(), e.getMessage());
            throw new InternalException("Could not create client", transactionId);
        }
    }

    @Override
    public void create(String username, String password) {
        ClientEntity client = new ClientEntity();
        client.setUsername(username);
        client.setRol(ROL_DEFAULT);
        client.setStatus(true);
        client.setPassword(passwordEncoder.encode(password));
        saveClient.save(client);
    }

    @Override
    public String login(String username, String password) {

        Optional<ClientEntity> clientDetail = obtainClient.findByUsername(username);

        if (clientDetail.isEmpty()) {
            LOGGER.error("The user does not exist {}", username);
            throw new InternalException("The user does not exist - " + username, "");
        }

        if (!passwordEncoder.matches(password, clientDetail.get().getPassword())) {
            throw new InternalException("Check the data provided", "");
        }

        ClientEntity client = clientDetail.get();

        CustomUserDetails customUserDetails = new CustomUserDetails(
                List.of(new SimpleGrantedAuthority(client.getRol())),
                client.getPassword(),
                client.getUsername(),
                true,
                true,
                true,
                client.isStatus(),
                client.getId(),
                "",
                ""
        );

        return jwtPort.generateToken(customUserDetails);
    }

    @Override
    public boolean isValidJwt(String jwt) {
        return jwtPort.isTokenValid(jwt);
    }

    @Override
    public String username(String jwt) {
        return jwtPort.extractUsername(jwt);
    }

    @Override
    @Transactional
    public void changePassword(String username, String password, String newPassword) {

        Optional<ClientEntity> clientDetail = obtainClient.findByUsername(username);

        if (clientDetail.isEmpty()) {
            throw new InternalException("The user does not exist - " + username, "");
        }

        if (!passwordEncoder.matches(password, clientDetail.get().getPassword())) {
            throw new InternalException("Invalid password", "");
        }

        ClientEntity client = clientDetail.get();
        client.setPassword(passwordEncoder.encode(newPassword));
        saveClient.save(client);
    }
}
