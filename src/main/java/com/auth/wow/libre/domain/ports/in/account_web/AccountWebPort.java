package com.auth.wow.libre.domain.ports.in.account_web;

import com.auth.wow.libre.domain.model.AccountWebModel;
import com.auth.wow.libre.domain.model.dto.AccountWebDto;
import com.auth.wow.libre.domain.model.dto.ValidateOtpDto;
import com.auth.wow.libre.domain.model.security.JwtDto;

public interface AccountWebPort {

    AccountWebModel save(AccountWebModel accountWebModel, String transactionId);

    AccountWebModel findByEmail(String email, String transactionId);

    JwtDto create(AccountWebDto accountWebDto, String transactionId);

    void recoverPassword(String email, String transactionId);

    void validateOtp(ValidateOtpDto request, String transactionId);

    void confirmation(String email, String confirmationCode, String transactionId);

}
