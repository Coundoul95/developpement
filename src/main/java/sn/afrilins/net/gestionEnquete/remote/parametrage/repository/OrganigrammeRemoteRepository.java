package sn.afrilins.net.gestionEnquete.remote.parametrage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.afrilins.net.gestionEnquete.remote.parametrage.domain.OrganigrammeRemote;

import java.util.Optional;

public interface OrganigrammeRemoteRepository extends JpaRepository<OrganigrammeRemote, Long> {
    Optional<OrganigrammeRemote> findFirstByCode(String code);
}
