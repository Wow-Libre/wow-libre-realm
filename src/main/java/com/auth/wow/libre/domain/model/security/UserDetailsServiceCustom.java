package com.auth.wow.libre.domain.model.security;

import com.auth.wow.libre.domain.model.Account;
import com.auth.wow.libre.domain.model.exception.BadRequestException;
import com.auth.wow.libre.domain.ports.out.account.ObtainAccountPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UserDetailsServiceCustom implements UserDetailsService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceCustom.class);

  private final ObtainAccountPort obtainAccountPort;


  public UserDetailsServiceCustom(ObtainAccountPort obtainAccountPort) {
    this.obtainAccountPort = obtainAccountPort;
  }

  @Override
  public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Account account = obtainAccountPort.findByUsername(username);

    if (account == null) {
      LOGGER.error("There is no associated user [{}]",
              username);
      throw new BadRequestException("There is no associated user", "");
    }

    return new CustomUserDetails(
            defaultRol(),
            account.password,
            account.username,
            true,
            true,
            true,
            true,
            account.id
    );
  }


  private List<GrantedAuthority> defaultRol() {
    return Collections.singletonList(new SimpleGrantedAuthority("CLIENT"));
  }
}
