package com.auth.wow.libre.domain.model.security;

import java.util.Date;

public class JwtDto {
  public String jwt;
  public String refreshToken;
  public Date expirationDate;

  public JwtDto(String jwt, String refreshToken, Date expirationDate) {
    this.jwt = jwt;
    this.refreshToken = refreshToken;
    this.expirationDate = expirationDate;
  }
}
