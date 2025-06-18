package sn.afrilins.net.gestionEnquete.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

@Profile(value = "!test")
@ConfigurationProperties(prefix = "config-db")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DatabaseProperties {

    String urldb;

    String password_asi;

    String username_asi;

    String driver;

    String username_param;

    String password_param;

    String password_tracer;

    String username_tracer;





}
