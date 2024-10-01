package com.auth.wow.libre.domain.model.security;

import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class UserDetailsServiceCustom implements UserDetailsService {


    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //AccountWebEntity account = obtainAccountWebPort.findByEmailAndStatusIsTrue(username)
        //  .orElseThrow(() -> new BadRequestException(CONSTANT_GENERIC_ERROR_ACCOUNT_IS_NOT_AVAILABLE + username
        //   , ""));


        return new CustomUserDetails(assignRol(""), "",
                "",
                true,
                true,
                true,
                true,
                1L,
                "",
                "es"
        );
    }


    private List<GrantedAuthority> assignRol(String name) {
        return Collections.singletonList(new SimpleGrantedAuthority(name));
    }


}
