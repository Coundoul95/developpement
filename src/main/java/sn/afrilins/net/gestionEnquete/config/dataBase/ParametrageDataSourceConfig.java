package sn.afrilins.net.gestionEnquete.config.dataBase;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@EntityScan(basePackages = "sn.afrilins.net.gestionEnquete.remote.parametrage.domain")
@EnableJpaRepositories(basePackages = "sn.afrilins.net.gestionEnquete.remote.parametrage.repository",
        entityManagerFactoryRef = "entityManagerFactory3",
        transactionManagerRef = "firstTransactionManager3")
@Configuration
@Profile("!test")
@AllArgsConstructor
@Slf4j
public class ParametrageDataSourceConfig {

    private final DatabaseProperties properties;
    @Bean
    public DataSource thirdDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(properties.getDriver());
        dataSource.setUrl(properties.getUrldb());
        dataSource.setUsername(properties.getUsername_param());
        dataSource.setPassword(properties.getPassword_param());
        return dataSource;
    }


    @Bean
    @Profile("!test")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory3(
            ParametrageDataSourceConfig thirdDataSourceConfig) {

        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        Map<String, Object> properties = new HashMap<>();
        properties.put(AvailableSettings.PHYSICAL_NAMING_STRATEGY, "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
        properties.put(AvailableSettings.IMPLICIT_NAMING_STRATEGY, "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy");
        em.setDataSource(thirdDataSourceConfig.thirdDataSource());
        em.setPackagesToScan("sn.afrilins.net.gestionImmeubleRapport.remote.parametrage.domain");
        properties.put(AvailableSettings.DIALECT, "org.hibernate.dialect.Oracle10gDialect");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaPropertyMap(properties);
        return em;
    }


    @Bean
    @Profile("!test")
    public PlatformTransactionManager firstTransactionManager3(
            @Qualifier("entityManagerFactory3") EntityManagerFactory
                    entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
