package sn.afrilins.net.gestionEnquete.util;

import org.keycloak.KeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Optional;

@Component
public class InfoUserService {

    @Value("${info.version}")
    String infoVersion;
    private final KeycloakSecurityContext keycloakSecurityContext;

    public InfoUserService(KeycloakSecurityContext keycloakSecurityContext) {
        this.keycloakSecurityContext = keycloakSecurityContext;
    }

    public Optional<String> getCurrentUsername() {
       if(infoVersion.equalsIgnoreCase("test")) {
           return Optional.of("test");
       }
        if(RequestContextHolder.getRequestAttributes()!=null){
            return Optional.ofNullable(keycloakSecurityContext.getToken().getPreferredUsername());
        }
        else{
            return Optional.of("kafka");
        }
    }

}
