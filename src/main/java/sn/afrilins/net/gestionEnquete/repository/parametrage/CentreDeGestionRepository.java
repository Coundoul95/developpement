package sn.afrilins.net.gestionEnquete.repository.parametrage;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.afrilins.net.gestionEnquete.domain.parametrage.CentreDeGestion;

import java.util.Optional;

public interface CentreDeGestionRepository extends JpaRepository<CentreDeGestion,Long> {

    Optional<CentreDeGestion> findFirstByCode(String code);
}
