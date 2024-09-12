package com.auth.wow.libre.domain.model.security;

import com.auth.wow.libre.domain.model.exception.BadRequestException;
import com.auth.wow.libre.domain.ports.out.account_web.ObtainAccountWebPort;
import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static com.auth.wow.libre.domain.model.constant.Constants.Errors.CONSTANT_GENERIC_ERROR_ACCOUNT_IS_NOT_AVAILABLE;

@Component
public class UserDetailsServiceCustom implements UserDetailsService {
    private final ObtainAccountWebPort obtainAccountWebPort;

    public UserDetailsServiceCustom(ObtainAccountWebPort obtainAccountWebPort) {
        this.obtainAccountWebPort = obtainAccountWebPort;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AccountWebEntity account = obtainAccountWebPort.findByEmailAndStatusIsTrue(username)
                .orElseThrow(() -> new BadRequestException(CONSTANT_GENERIC_ERROR_ACCOUNT_IS_NOT_AVAILABLE + username
                        , ""));


        return new CustomUserDetails(assignRol(account.getRolId().getName()), account.getPassword(),
                account.getEmail(),
                true,
                true,
                true,
                true,
                account.getId(),
                account.getAvatarUrl(),
                account.getLanguage()
        );
    }


    private List<GrantedAuthority> assignRol(String name) {
        return Collections.singletonList(new SimpleGrantedAuthority(name));
    }


}
