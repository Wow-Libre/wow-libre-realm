package com.auth.wow.libre.domain.ports.out.account_web;

import com.auth.wow.libre.infrastructure.entities.AccountWebEntity;

public interface ObtainAccountWebPort {
  AccountWebEntity findById(Long id);

}
