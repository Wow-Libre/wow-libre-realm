package com.auth.wow.libre.domain.model.security;

import com.auth.wow.libre.domain.model.exception.*;
import com.auth.wow.libre.domain.ports.out.user.*;
import com.auth.wow.libre.infrastructure.entities.auth.*;
import org.slf4j.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class UserDetailsServiceCustom implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceCustom.class);


    private final ObtainUser obtainUser;

    public UserDetailsServiceCustom(ObtainUser obtainUser) {
        this.obtainUser = obtainUser;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserEntity> client = obtainUser.findByUsername(username);

        if (client.isEmpty()) {
            LOGGER.error("The customer does not exist or their data is invalid.");
            throw new UnauthorizedException("Unauthorized Username", "");
        }

        UserEntity clientData = client.get();

        return new CustomUserDetails(assignRol(clientData.getRol()), clientData.getPassword(),
                clientData.getUsername(),
                true,
                true,
                true,
                clientData.isStatus(),
                clientData.getId(),
                "",
                "es"
        );
    }

    private List<GrantedAuthority> assignRol(String name) {
        return Collections.singletonList(new SimpleGrantedAuthority(name.toUpperCase()));
    }


}
