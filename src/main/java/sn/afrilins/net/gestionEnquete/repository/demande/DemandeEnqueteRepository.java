package sn.afrilins.net.gestionEnquete.repository.demande;

import com.querydsl.core.BooleanBuilder;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.demande.DemandeEnquete;
import sn.afrilins.net.gestionEnquete.domain.demande.QDemandeEnquete;
import sn.afrilins.net.gestionEnquete.domain.enume.TypeConcerne;

import java.time.LocalDateTime;
import java.util.List;

public interface DemandeEnqueteRepository extends JpaRepository<DemandeEnquete, Long>, QuerydslPredicateExecutor<DemandeEnquete> {

    @Query("SELECT d.etat.code, COUNT(d) FROM DemandeEnquete d WHERE (:utilisateurId IS NULL OR d.utilisateur.id = :utilisateurId) GROUP BY d.etat.code ")
    List<Object[]> countDemandesByEtat(@Param("utilisateurId") Long utilisateurId);

    // --- Compter par état (optionnellement par utilisateur) ---
    @Query("SELECT COUNT(d) " +
           "FROM DemandeEnquete d "+
           "WHERE (:etat IS NULL OR d.etat.code = :etat) "+
            " AND (:userId IS NULL OR d.utilisateur.id = :userId)")
    long countByEtatAndUtilisateur(@Param("etat") String etat,
                                   @Param("userId") Long userId);

    @Query(value = "SELECT TRUNC(d.created_at) as jour, COUNT(*) " +
            "FROM DEMANDE_DEMANDE_ENQUETE d " +
            "WHERE d.created_at >= :startDate " +
            "AND (:userId IS NULL OR d.utilisateur_id = :userId) " +
            "GROUP BY TRUNC(d.created_at) " +
            "ORDER BY jour",
            nativeQuery = true)
    List<Object[]> getDemandesCreeesParJour(@Param("userId") Long userId,
                                            @Param("startDate") LocalDateTime startDate);

    @Query(value = "SELECT TRUNC(d.date_validation) as jour, COUNT(*) " +
            "FROM DEMANDE_DEMANDE_ENQUETE d " +
            "WHERE d.date_validation >= :startDate " +
            "AND (:userId IS NULL OR d.utilisateur_id = :userId) " +
            "GROUP BY TRUNC(d.date_validation) " +
            "ORDER BY jour",
            nativeQuery = true)
    List<Object[]> getDemandesTraiteesParJour(@Param("userId") Long userId,
                                              @Param("startDate") LocalDateTime startDate);


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
