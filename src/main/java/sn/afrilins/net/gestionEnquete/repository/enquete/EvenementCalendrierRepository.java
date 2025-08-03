package sn.afrilins.net.gestionEnquete.repository.enquete;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.enquete.EvenementCalendrier;
import sn.afrilins.net.gestionEnquete.domain.enquete.QEvenementCalendrier;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EvenementCalendrierRepository extends JpaRepository<EvenementCalendrier, Long>, QuerydslPredicateExecutor<EvenementCalendrier> {

    Optional<EvenementCalendrier> findFirstByTitre(String titre);

    List<EvenementCalendrier> findByDate(LocalDate date);

    List<EvenementCalendrier> findByDateBetween(LocalDate debut, LocalDate fin);

    Page<EvenementCalendrier> findByDate(LocalDate date, Pageable pageable);

    Page<EvenementCalendrier> findByDateBetween(LocalDate debut, LocalDate fin, Pageable pageable);

    default Page<EvenementCalendrier> findAllEvenementCalendrier(
            String search,
            String titre,
            String heure,
            Integer duree,
            String priorite,
            LocalDate date,
            Long utilisateurId,
            String typeCode,
            Pageable pageable
    ) {
        QEvenementCalendrier evenement = QEvenementCalendrier.evenementCalendrier;
        BooleanBuilder predicate = new BooleanBuilder();

        // 🔍 Recherche globale
        if (search != null && !search.isBlank()) {
            BooleanBuilder searchPredicate = new BooleanBuilder();
            searchPredicate.or(evenement.titre.containsIgnoreCase(search));
            searchPredicate.or(evenement.heure.containsIgnoreCase(search));
            searchPredicate.or(evenement.priorite.containsIgnoreCase(search));
            searchPredicate.or(evenement.description.containsIgnoreCase(search));
            searchPredicate.or(evenement.type.code.containsIgnoreCase(search));
            searchPredicate.or(evenement.type.libelle.containsIgnoreCase(search));
            predicate.and(searchPredicate);
        }


        // 🔎 Filtres individuels
        if (titre != null && !titre.isBlank()) {
            predicate.and(evenement.titre.containsIgnoreCase(titre));
        }

        if(utilisateurId != null){
            predicate.and(evenement.utilisateur.id.eq(utilisateurId));
        }

        if(typeCode != null && !typeCode.isBlank()){
            predicate.and(evenement.type.code.eq(typeCode));
        }

        if (heure != null && !heure.isBlank()) {
            predicate.and(evenement.heure.eq(heure));
        }

        if (duree != null) {
            predicate.and(evenement.duree.eq(duree));
        }

        if (priorite != null && !priorite.isBlank()) {
            predicate.and(evenement.priorite.equalsIgnoreCase(priorite));
        }

        if (date != null) {
            predicate.and(evenement.date.eq(date));
        }

        return findAll(predicate, pageable);
    }

    List<EvenementCalendrier> findAllByUtilisateurIdAndDateBetween(Long utilisateurId, LocalDate debutSemaine, LocalDate finSemaine);
}

