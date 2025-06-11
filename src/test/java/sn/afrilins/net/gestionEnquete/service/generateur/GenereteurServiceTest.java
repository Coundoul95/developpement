package sn.afrilins.net.gestionEnquete.service.generateur;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import sn.afrilins.net.gestionImmeubleRapport.domain.generateur.GenerateurPatrimoine;
import sn.afrilins.net.gestionImmeubleRapport.exception.GiraException;
import sn.afrilins.net.gestionImmeubleRapport.repository.contrat.ContratLocationRepository;
import sn.afrilins.net.gestionImmeubleRapport.repository.contrat.DemandeContratRepository;
import sn.afrilins.net.gestionImmeubleRapport.repository.facture.FacturationLoyerRepository;
import sn.afrilins.net.gestionImmeubleRapport.repository.generateur.GenerateurPatrimoineRepository;
import sn.afrilins.net.gestionImmeubleRapport.repository.locataire.LocataireRepository;
import sn.afrilins.net.gestionImmeubleRapport.services.interfaces.generateur.GenerateurService;

import java.util.Map;

import static org.junit.Assert.*;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
public class GenereteurServiceTest {

    @Autowired
    private FacturationLoyerRepository factureRepository;

    @Autowired
    private DemandeContratRepository demandeContratRepository;

    @Autowired
    private ContratLocationRepository contratLocationRepository;

    @Autowired
    private LocataireRepository locataireRepository;

    @Autowired
    private GenerateurPatrimoineRepository generateurPatrimoineRepository;

    @Autowired
    private GenerateurService generateurService;


    @BeforeEach
    void setUp() {
        factureRepository.deleteAll();
        demandeContratRepository.deleteAll();
        contratLocationRepository.deleteAll();
        locataireRepository.deleteAll();
        generateurPatrimoineRepository.deleteAll();
    }

    @Test
    void shouldGenerateValueForFacture() {
        // Arrange : Insérer un générateur actif pour FAC
        GenerateurPatrimoine generateur = new GenerateurPatrimoine();
        generateur.setActif(1);
        generateur.setEntite("FAC");
        generateur.setPrefix("FAC-");
        generateur.setPadding("0");
        generateur.setSuffix("-2024");
        generateur.setSequence(5);
        generateurPatrimoineRepository.save(generateur);

        // Act
        Map<String, Object> result = generateurService.generateValue("FAC");

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("numero"));
        assertTrue(result.containsKey("sequence"));
        assertEquals("FAC-00001-2024", result.get("numero"));
        assertEquals(1, result.get("sequence"));
    }

    @Test
    void shouldThrowException_WhenGenerateurNotFound() {
        // Act & Assert
        GiraException exception = assertThrows(GiraException.class, () -> {
            generateurService.generateValue("INVALID");
        });

        assertNotNull(exception);
        assertEquals("405", exception.getCode());
    }
}
