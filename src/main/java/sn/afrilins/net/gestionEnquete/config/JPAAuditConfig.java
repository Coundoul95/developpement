package sn.afrilins.net.gestionEnquete.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Utilisateur;

@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JPAAuditConfig {

    private final AuditorAware auditorAware;

    @Bean
    public AuditorAware<Utilisateur> auditorProvider() {
        return auditorAware;
    }

}