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
        basePackages = "com.auth.wow.libre.infrastructure.repositories.world",
        entityManagerFactoryRef = "threeEntityManagerFactory",
        transactionManagerRef = "threeTransactionManager"
)
public class ThreeDataSourceConfig {

    @Value("${spring.datasource.three.url}")
    private String dbThreeHost;
    @Value("${spring.datasource.three.username}")
    private String dbThreeUsername;
    @Value("${spring.datasource.three.password}")
    private String dbThreePassword;
    @Value("${spring.datasource.three.driver-class-name}")
    private String dbThreeDriverPrimary;

    @Bean(name = "threeDataSource")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create()
                .url(dbThreeHost)
                .username(dbThreeUsername)
                .password(dbThreePassword)
                .driverClassName(dbThreeDriverPrimary)
                .build();
    }

    @Bean(name = "threeEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("threeDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.auth.wow.libre.infrastructure.entities.world")
                .persistenceUnit("three")
                .build();
    }

    @Bean(name = "threeTransactionManager")
    public PlatformTransactionManager secondaryTransactionManager(
            @Qualifier("threeEntityManagerFactory") EntityManagerFactory secondaryEntityManagerFactory) {
        return new JpaTransactionManager(secondaryEntityManagerFactory);
    }
}
