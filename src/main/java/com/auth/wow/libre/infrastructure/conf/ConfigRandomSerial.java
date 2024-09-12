package com.auth.wow.libre.infrastructure.conf;

import com.auth.wow.libre.infrastructure.util.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ConfigRandomSerial {

    @Value("${recover-password.random.config.otp.length:5}")
    private Integer idLengthOtp;
    @Value("${recover-password.random.config.otp.alphabet:abcdefghijklmnopqrstuvwxyz0123456789}")
    private String idAlphabetOtp;

    @Bean("recover-password")
    public RandomString configRandomStringSessionId() {
        return new RandomString(idLengthOtp, idAlphabetOtp);
    }

    @Bean("random-string")
    public RandomString configRandomStringReset() {
        return new RandomString(15, "abcdefghijklmnopqrstuvwxyz0123456789");
    }


}
