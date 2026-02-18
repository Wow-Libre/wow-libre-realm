package com.auth.wow.libre.infrastructure.conf;

import com.auth.wow.libre.infrastructure.util.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

@Component
public class ConfigRandomSerial {


    @Bean("recoverPassword")
    public RandomString configRandomStringSessionId() {
        return new RandomString(5, "abcdefghijklmnopqrstuvwxyz0123456789");
    }

    @Bean("randomString")
    public RandomString configRandomStringReset() {
        return new RandomString(15, "abcdefghijklmnopqrstuvwxyz0123456789");
    }

    @Bean("randomUsername")
    public RandomString configRandomUsernameReset() {
        return new RandomString(5, "abcdefghijklmnopqrstuvwxyz");
    }

}
