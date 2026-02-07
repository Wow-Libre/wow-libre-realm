package com.auth.wow.libre.infrastructure.conf.db;

import com.auth.wow.libre.domain.model.GameMasterCredentials;
import com.auth.wow.libre.infrastructure.conf.RealmProperties;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Devuelve las credenciales GM para SOAP del reino actual (o del reino indicado).
 * Lee de application.realms[].soap (username/password) por reino.
 */
@Component
public class RealmSoapCredentialsProvider {

    private final RealmProperties realmProperties;
    private final RealmDataSourceRegistry registry;

    public RealmSoapCredentialsProvider(RealmProperties realmProperties,
                                        RealmDataSourceRegistry registry) {
        this.realmProperties = realmProperties;
        this.registry = registry;
    }

    /**
     * Credenciales para el reino indicado. Si realmId es null, usa el reino por defecto.
     */
    public GameMasterCredentials getCredentials(Long realmId) {
        Long effectiveRealmId = realmId != null ? realmId : registry.getDefaultRealmId();
        return findRealmConfig(effectiveRealmId)
                .map(realm -> {
                    RealmProperties.SoapCredentials soap = realm.getSoap();
                    if (soap == null || soap.getUsername() == null || soap.getPassword() == null) {
                        throw new IllegalStateException(
                                "Realm " + effectiveRealmId + " has no soap credentials (application.realms[].soap.username/password)");
                    }
                    return new GameMasterCredentials(soap.getUsername(), soap.getPassword());
                })
                .orElseThrow(() -> new IllegalStateException(
                        "No realm config found for realm id: " + effectiveRealmId));
    }

    private Optional<RealmProperties.RealmConfig> findRealmConfig(Long realmId) {
        if (realmProperties.getRealms() == null) {
            return Optional.empty();
        }
        return realmProperties.getRealms().stream()
                .filter(r -> realmId.equals(r.getId()))
                .findFirst();
    }
}
