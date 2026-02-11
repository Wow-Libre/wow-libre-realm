package com.auth.wow.libre.infrastructure.conf.db;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.orm.jpa.*;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.*;
import org.springframework.orm.jpa.*;
import org.springframework.transaction.*;

import javax.sql.*;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.auth.wow.libre.infrastructure.repositories.characters",
        entityManagerFactoryRef = "secondaryEntityManagerFactory",
        transactionManagerRef = "secondaryTransactionManager"
)
public class SecondaryDataSourceConfig {

    @Bean(name = "secondaryDataSource")
    public DataSource secondaryDataSource(RealmDataSourceRegistry registry) {
        CharactersRoutingDataSource routingDataSource = new CharactersRoutingDataSource();
        routingDataSource.setTargetDataSources(registry.getCharactersDataSourcesMap());
        routingDataSource.setDefaultTargetDataSource(registry.getDefaultCharactersDataSource());
        routingDataSource.afterPropertiesSet();
        return routingDataSource;
    }

    @Bean(name = "secondaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("secondaryDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.auth.wow.libre.infrastructure.entities.characters")
                .persistenceUnit("secondary")
                .build();
    }

    @Bean(name = "secondaryTransactionManager")
    public PlatformTransactionManager secondaryTransactionManager(
            @Qualifier("secondaryEntityManagerFactory") EntityManagerFactory secondaryEntityManagerFactory) {
        return new JpaTransactionManager(secondaryEntityManagerFactory);
    }
}
