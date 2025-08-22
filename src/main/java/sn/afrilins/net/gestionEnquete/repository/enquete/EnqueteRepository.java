package sn.afrilins.net.gestionEnquete.repository.enquete;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.enquete.Enquete;
import sn.afrilins.net.gestionEnquete.domain.enquete.QEnquete;

import java.time.LocalDateTime;
import java.util.List;

public interface EnqueteRepository extends JpaRepository<Enquete, Long>, QuerydslPredicateExecutor<Enquete> {

    default Page<Enquete> readAllEnquete(
            String etatCode,
            Integer progression,
            LocalDateTime dateDebut,
            LocalDateTime dateFin,
            Boolean assignee,      // filtrer assignée/non assignée
            Long enqueteurId,      // filtrer par enquêteur
            Pageable pageable) {

        QEnquete enquete = QEnquete.enquete;

        BooleanBuilder predicate = new BooleanBuilder();

        if (etatCode != null && !etatCode.isEmpty()) {
            predicate.and(enquete.etat.code.eq(etatCode));
        }

        if (progression != null) {
            predicate.and(enquete.progression.eq(progression));
        }

        if (dateDebut != null) {
            predicate.and(enquete.dateDebut.goe(dateDebut)); // >= dateDebut
        }

        if (dateFin != null) {
            predicate.and(enquete.dateFin.loe(dateFin)); // <= dateFin
        }

        if (assignee != null) {
            if (assignee) {
                predicate.and(enquete.enqueteur.isNotNull());
            } else {
                predicate.and(enquete.enqueteur.isNull());
            }
        }

        if (enqueteurId != null) {
            predicate.and(enquete.enqueteur.id.eq(enqueteurId));
        }

        return findAll(predicate, pageable);
    }

}
