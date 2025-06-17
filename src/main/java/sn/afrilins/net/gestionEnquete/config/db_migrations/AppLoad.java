package sn.afrilins.net.gestionEnquete.config.db_migrations;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppLoad implements ApplicationRunner {

//    private final UrgenceRepository urgenceRepository;
//
//    private final TypeBudgetRepository typeBudgetRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
       // urgenceRepository.save(Urgence.builder().code("01").libelle("GENANT").build());
        //urgenceRepository.save(Urgence.builder().code("02").libelle("Bloquant").build());
        //urgenceRepository.save(Urgence.builder().code("03").libelle("Mineur").build());
      /*  typeBudgetRepository.findFirstByCode("01").orElse(
                typeBudgetRepository.save(TypeBudget.builder().code("01").libelle("Budget de production").build())
        );
        typeBudgetRepository.findFirstByCode("02").orElse(
                typeBudgetRepository.save(TypeBudget.builder().code("02").libelle("Budget de consommation").build())
        );
        typeBudgetRepository.findFirstByCode("03").orElse(
                typeBudgetRepository.save(TypeBudget.builder().code("02").libelle("Budget d'investissement").build())
        );*/
    }
}
