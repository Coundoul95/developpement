package sn.afrilins.net.gestionEnquete.config.dataBase;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import sn.afrilins.net.gestionEnquete.properties.DatabaseProperties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@EntityScan(basePackages = "sn.afrilins.net.gestionImmeubleRapport.domain;")
@EnableJpaRepositories(basePackages = "sn.afrilins.net.gestionEnquete.repository",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "firstTransactionManager")
@Configuration
@AllArgsConstructor
@Profile("!test")
@Slf4j
public class PrimaryDataSourceConfig {

    private final DatabaseProperties properties;

    @Bean
    @Primary
    @Profile("!test")
    public DataSource primaryDataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        log.info("driver class =name ================================== {}",properties.getDriver());
//        dataSource.setDriverClassName(properties.getDriver());
//        dataSource.setUrl(properties.getUrldb());
//        dataSource.setUsername(properties.getUsername_asi());
//        dataSource.setPassword(properties.getPassword_asi());
//        return dataSource;

        HikariConfig dataSource = new HikariConfig();
        dataSource.setDriverClassName(properties.getDriver());
        dataSource.setJdbcUrl(properties.getUrldb());
        dataSource.setUsername(properties.getUsername_asi());
        dataSource.setPassword(properties.getPassword_asi());
        dataSource.setMaximumPoolSize(10);
        dataSource.setMinimumIdle(5);
        dataSource.setConnectionTimeout(3000000);
        dataSource.setAutoCommit(false);
        return new HikariDataSource(dataSource);
    }


    @Bean
    @Primary
    @Profile("!test")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            PrimaryDataSourceConfig primaryDataSourceConfig) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(AvailableSettings.PHYSICAL_NAMING_STRATEGY, "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
        properties.put(AvailableSettings.IMPLICIT_NAMING_STRATEGY, "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy");
        properties.put(AvailableSettings.SHOW_SQL, true);
        properties.put(AvailableSettings.HBM2DDL_AUTO, "update");
        properties.put(AvailableSettings.DIALECT, "org.hibernate.dialect.Oracle10gDialect");
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(primaryDataSourceConfig.primaryDataSource());
        em.setPackagesToScan(new String[]{"sn.afrilins.net.gestionImmeubleRapport.domain"});
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean
    @Primary
    @Profile("!test")
    public PlatformTransactionManager firstTransactionManager(
            @Qualifier("entityManagerFactory") EntityManagerFactory
                    entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
