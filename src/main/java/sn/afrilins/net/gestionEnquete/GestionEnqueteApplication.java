package sn.afrilins.net.gestionEnquete;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.jms.annotation.EnableJms;
import sn.afrilins.net.brazzajms.notifiable.EnableKTNotifiable;


@EnableKTNotifiable
@EnableJms
@EnableFeignClients
@ConfigurationPropertiesScan(basePackages = "sn.afrilins.net.gestionEnquete.properties")
@SpringBootApplication
@EntityScan("sn.afrilins.net.gestionEnquete.domain")
public class GestionEnqueteApplication {
	public static void main(String[] args) {
		SpringApplication.run(GestionEnqueteApplication.class, args);
	}

//
//	@Bean
//	CommandLineRunner start(DocumentRepository repository) {
//		return args -> {
//			var document = repository.findById(1L);
//			if(document.isPresent()){
//				document.get().setChemin("/document/81b923e3-9fb0-4070-90c8-91c707952d4f.pdf");
//				repository.save(document.get());
//				System.out.println(document.toString());
//			}
//			final String name = "Fatou Badara Sall";
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
