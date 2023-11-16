package com.auth.wow.libre.domain.ports.in.jwt;


import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JwtPort {
  String generateToken(UserDetails userDetails);

  String extractUsername(String token);

  boolean isTokenValid(String token, UserDetails userDetails);

  String generateRefreshToken(UserDetails userDetails);

  Date extractExpiration(String token);
}
