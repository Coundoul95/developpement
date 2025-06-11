package sn.afrilins.net.gestionEnquete.service.configuration;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.spi.KeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Principal;
import java.util.Set;

//@Configuration
public class KeycloakTestConfig {

//    @Bean
    public KeycloakSecurityContext keycloakSecurityContext() {
        AccessToken accessToken = new AccessToken();
        accessToken.setPreferredUsername("admin@afrilins");

        return new KeycloakSecurityContext("fake-token", accessToken, null, null);
    }

//    @Bean
    public KeycloakAuthenticationToken keycloakAuthenticationToken(KeycloakSecurityContext keycloakSecurityContext) {
        KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal =
                new KeycloakPrincipal<>("admin@afrilins", keycloakSecurityContext);

        KeycloakAccount keycloakAccount = new KeycloakAccount() {
//            @Override
            public Principal getPrincipal() {
                return keycloakPrincipal;
            }

//            @Override
            public Set<String> getRoles() {
                return Set.of("ROLE_ADMIN");
            }
        };

        return new KeycloakAuthenticationToken(keycloakAccount, false);
    }
}

