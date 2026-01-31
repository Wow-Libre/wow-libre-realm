package com.auth.wow.libre.infrastructure.conf.db;

import com.auth.wow.libre.infrastructure.conf.RealmProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Registro de DataSources por reino (characters y world).
 * Construye un DataSource por cada reino definido en application.realms.
 */
@Configuration
public class RealmDataSourceRegistry {

    private final RealmProperties realmProperties;

    private Map<Object, DataSource> charactersDataSources;
    private Map<Object, DataSource> worldDataSources;
    private DataSource defaultCharactersDataSource;
    private DataSource defaultWorldDataSource;
    private Long defaultRealmId;

    public RealmDataSourceRegistry(RealmProperties realmProperties) {
        this.realmProperties = realmProperties;
        buildDataSources();
    }

    private void buildDataSources() {
        if (realmProperties.getRealms() == null || realmProperties.getRealms().isEmpty()) {
            throw new IllegalStateException(
                    "application.realms must define at least one realm (characters + world datasources)");
        }

        charactersDataSources = new HashMap<>();
        worldDataSources = new HashMap<>();

        for (RealmProperties.RealmConfig realm : realmProperties.getRealms()) {
            Long id = realm.getId();
            if (id == null) {
                throw new IllegalStateException("Realm id is required for each realm");
            }

            if (realm.getCharacters() != null) {
                DataSource ds = buildDataSource(realm.getCharacters());
                charactersDataSources.put(id, ds);
            }
            if (realm.getWorld() != null) {
                DataSource ds = buildDataSource(realm.getWorld());
                worldDataSources.put(id, ds);
            }

            if (defaultRealmId == null) {
                defaultRealmId = id;
                defaultCharactersDataSource = charactersDataSources.get(id);
                defaultWorldDataSource = worldDataSources.get(id);
            }
        }

        if (defaultCharactersDataSource == null || defaultWorldDataSource == null) {
            throw new IllegalStateException(
                    "First realm must define both characters and world datasources");
        }
    }

    private static DataSource buildDataSource(RealmProperties.DatasourceConfig config) {
        return DataSourceBuilder.create()
                .url(config.getUrl())
                .username(config.getUsername())
                .password(config.getPassword())
                .driverClassName(config.getDriverClassName())
                .build();
    }

    public Map<Object, DataSource> getCharactersDataSourcesMap() {
        return charactersDataSources;
    }

    public Map<Object, DataSource> getWorldDataSourcesMap() {
        return worldDataSources;
    }

    public DataSource getDefaultCharactersDataSource() {
        return defaultCharactersDataSource;
    }

    public DataSource getDefaultWorldDataSource() {
        return defaultWorldDataSource;
    }

    public Long getDefaultRealmId() {
        return defaultRealmId;
    }
}
