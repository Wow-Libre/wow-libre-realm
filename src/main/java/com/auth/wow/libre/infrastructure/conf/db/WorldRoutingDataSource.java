package com.auth.wow.libre.infrastructure.conf.db;

import com.auth.wow.libre.infrastructure.context.RealmContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * DataSource que enruta a la BD de world del reino actual (RealmContext).
 */
public class WorldRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return RealmContext.getCurrentRealmId();
    }
}
