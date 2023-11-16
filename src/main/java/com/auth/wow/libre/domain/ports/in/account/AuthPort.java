package com.auth.wow.libre.domain.ports.in.account;

import com.auth.wow.libre.domain.model.security.JwtDto;

public interface AuthPort {
  JwtDto login(String email, String password, String transactionId);
}
