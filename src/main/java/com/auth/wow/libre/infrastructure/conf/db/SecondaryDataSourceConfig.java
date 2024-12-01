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
        basePackages = "com.auth.wow.libre.infrastructure.repositories.characters",
        entityManagerFactoryRef = "secondaryEntityManagerFactory",
        transactionManagerRef = "secondaryTransactionManager"
)
public class SecondaryDataSourceConfig {

    @Value("${spring.datasource.secondary.url}")
    private String dbSecondaryHost;
    @Value("${spring.datasource.secondary.username}")
    private String dbSecondaryUsername;
    @Value("${spring.datasource.secondary.password}")
    private String dbSecondaryPassword;
    @Value("${spring.datasource.secondary.driver-class-name}")
    private String dbSecondaryDriverPrimary;

    @Bean(name = "secondaryDataSource")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create()
                .url(dbSecondaryHost)
                .username(dbSecondaryUsername)
                .password(dbSecondaryPassword)
                .driverClassName(dbSecondaryDriverPrimary)
                .build();
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
