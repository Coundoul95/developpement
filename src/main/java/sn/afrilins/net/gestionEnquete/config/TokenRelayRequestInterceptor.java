package sn.afrilins.net.gestionEnquete.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.keycloak.KeycloakSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TokenRelayRequestInterceptor implements RequestInterceptor {
    private final Logger log = LoggerFactory.getLogger(TokenRelayRequestInterceptor.class);

    public static final String AUTHORIZATION = "Authorization";
    private final KeycloakSecurityContext keycloakSecurityContext;


    public TokenRelayRequestInterceptor(KeycloakSecurityContext keycloakSecurityContext) {
        super();

        this.keycloakSecurityContext = keycloakSecurityContext;
    }

    @Override
    public void apply(RequestTemplate template) {
        String token = "Bearer " + keycloakSecurityContext.getTokenString();
        log.info("@@@@@@@@ token intercepted feign @@@@@@{}", token);
        template.header("Authorization", token);
    }
}
