package sn.afrilins.net.gestionEnquete.config.db_migrations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import sn.afrilins.net.gestionEnquete.domain.enquete.EtatAutreInfo;
import sn.afrilins.net.gestionEnquete.domain.enquete.EtatEnquete;
import sn.afrilins.net.gestionEnquete.domain.demande.EtatDemande;
import sn.afrilins.net.gestionEnquete.domain.enquete.EtatSourceInfo;
import sn.afrilins.net.gestionEnquete.domain.enquete.TypeSource;
import sn.afrilins.net.gestionEnquete.repository.enquete.EtatAutreInfoRepository;
import sn.afrilins.net.gestionEnquete.repository.enquete.EtatEnqueteRepository;
import sn.afrilins.net.gestionEnquete.repository.demande.EtatDemandeRepository;
import sn.afrilins.net.gestionEnquete.repository.enquete.EtatSourceInfoRepository;
import sn.afrilins.net.gestionEnquete.repository.enquete.TypeSourceRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppLoad implements ApplicationRunner {

    private final EtatAutreInfoRepository etatAutreInfoRepository;
    private final EtatEnqueteRepository etatEnqueteRepository;
    private final EtatDemandeRepository etatDemandeRepository;
    private final EtatSourceInfoRepository etatSourceInfoRepository;
    private final TypeSourceRepository typeSourceRepository;

    @Override
    public void run(ApplicationArguments args) {
        log.info("=== Initialisation des états de référence ===");

        // -------------------------
        // ÉTATS AUTRE INFO
        // -------------------------
        createEtatAutreInfoIfNotExists("00", "En attente");
        createEtatAutreInfoIfNotExists("01", "Validé");

        // -------------------------
        // ÉTATS ENQUETE
        // -------------------------
        createEtatEnqueteIfNotExists("00", "En attente");
        createEtatEnqueteIfNotExists("01", "En cours");
        createEtatEnqueteIfNotExists("02", "Terminée");
        createEtatEnqueteIfNotExists("03", "En validation");
        createEtatEnqueteIfNotExists("04", "Validée");
        createEtatEnqueteIfNotExists("05", "En révision");
        createEtatEnqueteIfNotExists("06", "Annulée");


        // -------------------------
        // ÉTATS DEMANDE
        // -------------------------
        createEtatDemandeIfNotExists("00", "En attente");
        createEtatDemandeIfNotExists("01", "Valider");
        createEtatDemandeIfNotExists("02", "Rejeter");
        createEtatDemandeIfNotExists("03", "En complèment");
        createEtatDemandeIfNotExists("04", "Annuler");

        // -------------------------
        // ÉTATS SOURCE INFO
        // -------------------------
        createEtatSourceInfoIfNotExists("00", "En attente de validation");
        createEtatSourceInfoIfNotExists("01", "Actif");
        createEtatSourceInfoIfNotExists("02", "Inactif");
        createEtatSourceInfoIfNotExists("03", "Archivée");
        createEtatSourceInfoIfNotExists("04", "Invalide / Non fiable");


        // -------------------------
        // TYPES SOURCE
        // -------------------------
        createTypeSourceIfNotExists("TEMOIN", "Déclaration de témoin");
        createTypeSourceIfNotExists("DOCUMENT", "Document officiel");
        createTypeSourceIfNotExists("OBSERVATION", "Observation terrain");
        createTypeSourceIfNotExists("NUMERIQUE", "Source numérique / en ligne");
        createTypeSourceIfNotExists("INTERNE", "Source interne");
        createTypeSourceIfNotExists("EXTERNE", "Source externe");

        log.info("=== Initialisation terminée ===");
    }

    private void createEtatAutreInfoIfNotExists(String code, String libelle) {
        etatAutreInfoRepository.findFirstByCode(code).orElseGet(() -> {
            log.info("Création EtatAutreInfo [{} - {}]", code, libelle);
            return etatAutreInfoRepository.save(
                    EtatAutreInfo.builder()
                            .code(code)
                            .libelle(libelle)
                            .build()
            );
        });
    }

    private void createEtatEnqueteIfNotExists(String code, String libelle) {
        etatEnqueteRepository.findFirstByCode(code).orElseGet(() -> {
            log.info("Création EtatEnquete [{} - {}]", code, libelle);
            return etatEnqueteRepository.save(
                    EtatEnquete.builder()
                            .code(code)
                            .libelle(libelle)
                            .build()
            );
        });
    }

    private void createEtatDemandeIfNotExists(String code, String libelle) {
        etatDemandeRepository.findFirstByCode(code).orElseGet(() -> {
            log.info("Création EtatDemande [{} - {}]", code, libelle);
            return etatDemandeRepository.save(
                    EtatDemande.builder()
                            .code(code)
                            .libelle(libelle)
                            .build()
            );
        });
    }

    private void createEtatSourceInfoIfNotExists(String code, String libelle) {
        etatSourceInfoRepository.findFirstByCode(code).orElseGet(() -> {
            log.info("Création EtatSourceInfo [{} - {}]", code, libelle);
            return etatSourceInfoRepository.save(
                    EtatSourceInfo.builder()
                            .code(code)
                            .libelle(libelle)
                            .build()
            );
        });
    }

    private void createTypeSourceIfNotExists(String code, String libelle) {
        typeSourceRepository.findFirstByCode(code).orElseGet(() -> {
            log.info("Création TypeSource [{} - {}]", code, libelle);
            return typeSourceRepository.save(
                    TypeSource.builder()
                            .code(code)
                            .libelle(libelle)
                            .build()
            );
        });
    }
}
