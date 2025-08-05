package sn.afrilins.net.gestionEnquete;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import sn.afrilins.net.brazzajms.notifiable.EnableKTNotifiable;
import sn.afrilins.net.gestionEnquete.domain.demande.Concerne;
import sn.afrilins.net.gestionEnquete.domain.enume.TypeConcerne;
import sn.afrilins.net.gestionEnquete.repository.demande.ConcerneRepository;


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

	@Bean
	CommandLineRunner start(ConcerneRepository repository){
		return  args -> {
//			repository.save(Concerne.builder().id(1l).type(TypeConcerne.EMPLOYEUR).numero("778133537").regionSocial("Dakar, Diamniadio").build());
		};
	}


}
