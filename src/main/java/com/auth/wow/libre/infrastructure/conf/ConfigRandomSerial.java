package com.auth.wow.libre.infrastructure.conf;

import com.auth.wow.libre.infrastructure.util.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

@Component
public class ConfigRandomSerial {


    @Bean("recover-password")
    public RandomString configRandomStringSessionId() {
        return new RandomString(5, "abcdefghijklmnopqrstuvwxyz0123456789");
    }

    @Bean("random-string")
    public RandomString configRandomStringReset() {
        return new RandomString(15, "abcdefghijklmnopqrstuvwxyz0123456789");
    }

    @Bean("random-username")
    public RandomString configRandomUsernameReset() {
        return new RandomString(5, "abcdefghijklmnopqrstuvwxyz");
    }

}
