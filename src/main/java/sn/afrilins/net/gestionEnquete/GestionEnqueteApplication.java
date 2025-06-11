package sn.afrilins.net.gestionEnquete;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.jms.annotation.EnableJms;
import sn.afrilins.net.brazzajms.notifiable.EnableKTNotifiable;


@EnableKTNotifiable
@EnableJms
@EnableFeignClients
@ConfigurationPropertiesScan(basePackages = "sn.afrilins.net.gestionEnquete.properties")
@SpringBootApplication
public class GestionEnqueteApplication {
	public static void main(String[] args) {
		SpringApplication.run(GestionEnqueteApplication.class, args);
	}

}
