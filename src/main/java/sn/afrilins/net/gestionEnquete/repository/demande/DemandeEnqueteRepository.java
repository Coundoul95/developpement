package sn.afrilins.net.gestionEnquete.repository.demande;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.demande.DemandeEnquete;
import sn.afrilins.net.gestionEnquete.domain.demande.EtatDemande;
import sn.afrilins.net.gestionEnquete.domain.demande.QDemandeEnquete;
import sn.afrilins.net.gestionEnquete.domain.enume.TypeConcerne;

import java.util.Objects;

public interface DemandeEnqueteRepository extends JpaRepository<DemandeEnquete, Long>, QuerydslPredicateExecutor<DemandeEnquete> {

    default Page<DemandeEnquete> findAllDemandeEnquete(
            Long utilisateurId,
            Boolean urgent,
            String objet,
            String type,
            String etat,
            String etatEnquete,
            Integer priorite,
            String search,
            Pageable pageable) {

        QDemandeEnquete demande = QDemandeEnquete.demandeEnquete;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (utilisateurId != null) {
            booleanBuilder.and(demande.utilisateur.id.eq(utilisateurId));
        }

        if (urgent != null) {
            booleanBuilder.and(demande.urgent.eq(urgent));
        }

        if (objet != null && !objet.isEmpty()) {
            booleanBuilder.and(demande.objet.containsIgnoreCase(objet));
        }

        if (type != null) {
            booleanBuilder.and(demande.concerne.type.eq(TypeConcerne.fromValue(type)));
        }

        if (etat != null && !etat.isEmpty()) {
            booleanBuilder.and(demande.etat.code.equalsIgnoreCase(etat)); // Assumes EtatDemande has a `code` or similar
        }

        if (etatEnquete != null && !etatEnquete.isEmpty()) {
            booleanBuilder.and(demande.enquete.isNotNull()
                    .and(demande.enquete.etat.code.eq(etatEnquete)));
        }

        if (priorite != null) {
            booleanBuilder.and(demande.priorite.eq(priorite));
        }

        if (search != null && !search.isEmpty()) {
            BooleanBuilder searchPredicate = new BooleanBuilder();
            searchPredicate.or(demande.objet.containsIgnoreCase(search));
            searchPredicate.or(demande.description.containsIgnoreCase(search));
            searchPredicate.or(demande.reference.containsIgnoreCase(search));
            booleanBuilder.and(searchPredicate);
        }

        return findAll(booleanBuilder, pageable);
    }
}
