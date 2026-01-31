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
        basePackages = "com.auth.wow.libre.infrastructure.repositories.world",
        entityManagerFactoryRef = "threeEntityManagerFactory",
        transactionManagerRef = "threeTransactionManager"
)
public class ThreeDataSourceConfig {

    @Bean(name = "threeDataSource")
    public DataSource threeDataSource(RealmDataSourceRegistry registry) {
        WorldRoutingDataSource routingDataSource = new WorldRoutingDataSource();
        routingDataSource.setTargetDataSources(registry.getWorldDataSourcesMap());
        routingDataSource.setDefaultTargetDataSource(registry.getDefaultWorldDataSource());
        routingDataSource.afterPropertiesSet();
        return routingDataSource;
    }

    @Bean(name = "threeEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean threeEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("threeDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.auth.wow.libre.infrastructure.entities.world")
                .persistenceUnit("three")
                .build();
    }

    @Bean(name = "threeTransactionManager")
    public PlatformTransactionManager threeTransactionManager(
            @Qualifier("threeEntityManagerFactory") EntityManagerFactory threeEntityManagerFactory) {
        return new JpaTransactionManager(threeEntityManagerFactory);
    }
}
