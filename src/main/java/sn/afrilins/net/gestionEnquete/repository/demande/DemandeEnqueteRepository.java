package sn.afrilins.net.gestionEnquete.repository.demande;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.demande.DemandeEnquete;

public interface DemandeEnqueteRepository extends JpaRepository<DemandeEnquete, Long>, QuerydslPredicateExecutor<DemandeEnquete> {


}
