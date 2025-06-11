package sn.afrilins.net.gestionEnquete.remote.parametrage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.afrilins.net.gestionEnquete.remote.parametrage.domain.CentreDeGestionRemote;

import java.util.Optional;

public interface CentreDeGestionRemoteRepository extends JpaRepository<CentreDeGestionRemote, Long> {

    Optional<CentreDeGestionRemote> findCentreDeGestionRemoteByCode(String code);
}
