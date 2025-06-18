package sn.afrilins.net.gestionEnquete;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import sn.afrilins.net.brazzajms.notifiable.EnableKTNotifiable;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Utilisateur;
import sn.afrilins.net.gestionEnquete.repository.UtilisateurRepository;

import java.util.Optional;


@EnableKTNotifiable
@EnableJms
@EnableFeignClients
@ConfigurationPropertiesScan(basePackages = "sn.afrilins.net.gestionEnquete.properties")
@SpringBootApplication
public class GestionEnqueteApplication {
	public static void main(String[] args) {
		SpringApplication.run(GestionEnqueteApplication.class, args);
	}


//	@Bean
//	CommandLineRunner start(UtilisateurRepository repository) {
//		return args -> {
//			final String name = "Elage Ciss";
//			Optional<Utilisateur> existingUser = repository.findByUsername(name);
//
//			if (existingUser.isEmpty()) {
//				Utilisateur newUser = Utilisateur.builder()
//						.username(name)
//						.build();
//
//				repository.save(newUser);
//			}
//		};
//	}

}
