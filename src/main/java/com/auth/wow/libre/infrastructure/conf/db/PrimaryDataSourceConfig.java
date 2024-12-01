package com.auth.wow.libre.infrastructure.conf.db;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.jdbc.*;
import org.springframework.boot.orm.jpa.*;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.*;
import org.springframework.orm.jpa.*;
import org.springframework.transaction.*;

import javax.sql.*;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.auth.wow.libre.infrastructure.repositories.auth",
        entityManagerFactoryRef = "primaryEntityManagerFactory",
        transactionManagerRef = "primaryTransactionManager"
)
public class PrimaryDataSourceConfig {

    @Value("${spring.datasource.primary.url}")
    private String dbPrimaryHost;
    @Value("${spring.datasource.primary.username}")
    private String dbPrimaryUsername;
    @Value("${spring.datasource.primary.password}")
    private String dbPrimaryPassword;
    @Value("${spring.datasource.primary.driver-class-name}")
    private String dbDriverPrimary;

    @Primary
    @Bean(name = "primaryDataSource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create()
                .url(dbPrimaryHost)
                .username(dbPrimaryUsername)
                .password(dbPrimaryPassword)
                .driverClassName(dbDriverPrimary)
                .build();
    }

    @Primary
    @Bean(name = "primaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("primaryDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.auth.wow.libre.infrastructure.entities.auth")
                .persistenceUnit("primary")
                .build();
    }

    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("primaryEntityManagerFactory") EntityManagerFactory primaryEntityManagerFactory) {
        return new JpaTransactionManager(primaryEntityManagerFactory);
    }

}
