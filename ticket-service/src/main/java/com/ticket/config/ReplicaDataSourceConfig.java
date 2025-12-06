package com.ticket.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.ticket.adapters.out.persistence.replica",
    entityManagerFactoryRef = "replicaEntityManagerFactory",
    transactionManagerRef = "replicaTransactionManager"
)
public class ReplicaDataSourceConfig {

    @Bean
    @ConfigurationProperties("replica.datasource")
    public DataSourceProperties replicaDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource replicaDataSource() {
        return replicaDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean replicaEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(replicaDataSource())
                .packages("com.ticket.adapters.out.persistence.replica")
                .persistenceUnit("replica")
                .build();
    }

    @Bean
    public PlatformTransactionManager replicaTransactionManager(
            @Qualifier("replicaEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
