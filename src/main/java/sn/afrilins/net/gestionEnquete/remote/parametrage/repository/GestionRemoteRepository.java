package sn.afrilins.net.gestionEnquete.remote.parametrage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.remote.parametrage.domain.GestionRemote;

import java.util.Optional;

public interface GestionRemoteRepository extends JpaRepository<GestionRemote, Long>, QuerydslPredicateExecutor<GestionRemote> {
    Optional<GestionRemote> findByCode(String code);
}
