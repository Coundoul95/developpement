package sn.afrilins.net.gestionEnquete.repository.enquete;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.enquete.Enquete;

public interface EnqueteRepository extends JpaRepository<Enquete, Long>, QuerydslPredicateExecutor<Enquete> {
}
