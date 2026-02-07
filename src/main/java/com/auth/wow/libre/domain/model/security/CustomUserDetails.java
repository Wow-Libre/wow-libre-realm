package com.auth.wow.libre.domain.model.security;

import lombok.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;

import java.util.*;

public class CustomUserDetails implements UserDetails {

    @Getter
    private final Long id;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String password;
    private final String username;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;
    @Getter
    private final Long realmId;
    @Getter
    private final String emulator;
    @Getter
    private final Integer expansionId;

    public CustomUserDetails(Collection<? extends GrantedAuthority> authorities, String password, String username,
                             boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired,
                             boolean enabled, Long id, Long realmId, String emulator, Integer expansionId) {
        this.authorities = authorities;
        this.password = password;
        this.username = username;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.id = id;
        this.realmId = realmId;
        this.emulator = emulator;
        this.expansionId = expansionId;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
