package sn.afrilins.net.gestionEnquete.remote.parametrage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.afrilins.net.gestionEnquete.remote.parametrage.domain.SiteRemote;

import java.util.Optional;

public interface SiteRemoteRepository extends JpaRepository<SiteRemote, Long> {

    Optional<SiteRemote> findFirstByCode(String code);
}
