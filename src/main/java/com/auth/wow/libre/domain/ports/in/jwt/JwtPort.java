package com.auth.wow.libre.domain.ports.in.jwt;


import com.auth.wow.libre.domain.model.security.*;
import org.springframework.security.core.*;

import java.util.*;

public interface JwtPort {
    String generateToken(CustomUserDetails userDetails);

    String extractUsername(String token);

    Long extractRealmId(String token);

    Long extractExpansionId(String token);

    String extractEmulator(String token);

    boolean isTokenValid(String token);

    String generateRefreshToken(CustomUserDetails userDetails);

    Date extractExpiration(String token);

    Collection<GrantedAuthority> extractRoles(String token);

}
