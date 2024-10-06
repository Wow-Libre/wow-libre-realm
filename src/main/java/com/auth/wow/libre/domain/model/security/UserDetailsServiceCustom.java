package com.auth.wow.libre.domain.model.security;

import com.auth.wow.libre.infrastructure.conf.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class UserDetailsServiceCustom implements UserDetailsService {

    private final Configurations configurations;

    public UserDetailsServiceCustom(Configurations configurations) {
        this.configurations = configurations;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new CustomUserDetails(assignRol(), configurations.getAuthPassWowLibre(),
                configurations.getLoginUsername(),
                true,
                true,
                true,
                true,
                1L,
                "",
                "es"
        );
    }


    private List<GrantedAuthority> assignRol() {
        return Collections.singletonList(new SimpleGrantedAuthority("CLIENT"));
    }


}
