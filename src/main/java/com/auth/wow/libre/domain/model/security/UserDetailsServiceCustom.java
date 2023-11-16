package com.auth.wow.libre.domain.model.security;

import com.auth.wow.libre.domain.model.Account;
import com.auth.wow.libre.domain.ports.out.account.ObtainAccountPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDetailsServiceCustom implements UserDetailsService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceCustom.class);

  private final ObtainAccountPort obtainAccountPort;


  public UserDetailsServiceCustom(ObtainAccountPort obtainAccountPort) {
    this.obtainAccountPort = obtainAccountPort;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Account account = obtainAccountPort.findByUsername(username);

    if (account == null) {
      LOGGER.error("El usuario no existe Username[{}]",
              username);
      throw new RuntimeException("El usuario no existe");
    }

    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("CLIENT"));

    return new User(account.getUsername(), account.getPassword(),
            true, true, true, true, authorities);
  }


}
