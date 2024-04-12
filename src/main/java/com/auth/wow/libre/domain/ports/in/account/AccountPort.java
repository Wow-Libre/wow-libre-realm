package com.auth.wow.libre.domain.ports.in.account;

import com.auth.wow.libre.domain.model.dto.AccountDetail;
import com.auth.wow.libre.domain.model.dto.AccountDto;
import com.auth.wow.libre.domain.model.dto.ChangePasswordAccountDto;
import com.auth.wow.libre.domain.model.dto.UpdateAccountDto;
import com.auth.wow.libre.domain.model.dto.WebPasswordAccountDto;
import org.apache.commons.codec.DecoderException;

public interface AccountPort {
  void create(AccountDto account, String transactionId);

  AccountDetail obtain(String username, String transactionId);

  void updated(String username, UpdateAccountDto account, String transactionId);

  void gameChangePassword(String username, ChangePasswordAccountDto changePasswordAccountDto,
                          String transactionId) throws DecoderException;

  void webChangePassword(String username, WebPasswordAccountDto webPasswordAccountDto,
                         String transactionId);

  void validateEmail(String email, String otp, String transactionId);

  boolean searchEmail(String search,  String transactionId);

}
