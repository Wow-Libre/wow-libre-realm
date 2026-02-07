package com.auth.wow.libre.infrastructure.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "application")
public class RealmProperties {

    private List<RealmConfig> realms = new ArrayList<>();

    @Data
    public static class RealmConfig {
        private Long id;
        private String name;
        private DatasourceConfig characters;
        private DatasourceConfig world;
        /** Credenciales GM para SOAP (Basic Auth) hacia el emulador de este reino. */
        private SoapCredentials soap;
    }

    @Data
    public static class SoapCredentials {
        private String username;
        private String password;
    }

    @Data
    public static class DatasourceConfig {
        private String url;
        private String username;
        private String password;
        private String driverClassName;
    }
}
