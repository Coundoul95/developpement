package sn.afrilins.net.gestionEnquete.repository.enquete;

import com.querydsl.core.BooleanBuilder;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.demande.QConcerne;
import sn.afrilins.net.gestionEnquete.domain.demande.QDemandeEnquete;
import sn.afrilins.net.gestionEnquete.domain.enquete.Enquete;
import sn.afrilins.net.gestionEnquete.domain.enquete.QEnquete;
import sn.afrilins.net.gestionEnquete.domain.enume.TypeConcerne;
import sn.afrilins.net.gestionEnquete.services.dto.demande.demande_enquete.response.DemandeValideEnqueteStatsDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface EnqueteRepository extends JpaRepository<Enquete, Long>, QuerydslPredicateExecutor<Enquete> {
    @Query("SELECT COUNT(e) FROM Enquete e WHERE e.etat.code = :codeEtat")
    long countByEtat(String codeEtat);

    @Query("SELECT e.etat.code, COUNT(e) FROM Enquete e WHERE (:utilisateurId IS NULL OR e.enqueteur.id = :utilisateurId) GROUP BY e.etat.code ")
    List<Object[]> countEnquetesByEtat(@Param("utilisateurId") Long utilisateurId);

    //    @Query("SELECT COUNT(e) FROM Enquete e WHERE e.demandeEnquete.dateEcheance IS NOT NULL " +
//            "AND e.demandeEnquete.dateEcheance <= CURRENT_DATE + 7")
//    long countEcheancesProches();
    @Query("SELECT COUNT(e) " +
            "FROM Enquete e " +
            "WHERE e.demandeEnquete.dateEcheance IS NOT NULL " +
            "AND e.demandeEnquete.dateEcheance <= CURRENT_DATE + 7 " +
            "AND (:enqueteurId IS NULL OR e.enqueteur.id = :enqueteurId)")
    long countEcheancesProches(@Param("enqueteurId") Long enqueteurId);

    @Query("SELECT COUNT(e) " +
            "FROM Enquete e " +
            "WHERE e.demandeEnquete.urgent = true " +
            "AND (:enqueteurId IS NULL OR e.enqueteur.id = :enqueteurId)")
    long countEnquetesUrgentes(@Param("enqueteurId") Long enqueteurId);

    @Query("SELECT AVG(e.progression) " +
            "FROM Enquete e " +
            "WHERE (:enqueteurId IS NULL OR e.enqueteur.id = :enqueteurId)")
    Double averageProgression(@Param("enqueteurId") Long enqueteurId);


    @Query("SELECT e " +
            "FROM Enquete e " +
            "WHERE e.demandeEnquete.urgent = true " +
            "AND (:enqueteurId IS NULL OR e.enqueteur.id = :enqueteurId)")
    List<Enquete> findEnquetesUrgentes(@Param("enqueteurId") Long enqueteurId);


    @Query("SELECT " +
            "SUM(CASE WHEN e.etat.code IN ('01','03') THEN 1 ELSE 0 END), " + // totalEnCours
            "SUM(CASE WHEN d.urgent = true OR d.priorite = 1 THEN 1 ELSE 0 END), " + // prioriteHaute
            "SUM(CASE WHEN e.etat.code = '03' THEN 1 ELSE 0 END), " + // enValidation
            "SUM(CASE WHEN d.dateEcheance < CURRENT_TIMESTAMP AND e.etat.code <> '02' THEN 1 ELSE 0 END) " + // enRetard
            "FROM Enquete e " +
            "JOIN e.demandeEnquete d " +
            "WHERE (:utilisateurId IS NULL OR d.utilisateur.id = :utilisateurId)")
    List<Object[]> getStatsEtatEnquete(@Param("utilisateurId") Long utilisateurId);


    default Page<Enquete> readAllEnquete(
            String etatCode,
            Integer progression,
            LocalDateTime dateDebut,
            LocalDateTime dateFin,
            Boolean assignee,      // filtrer assignée/non assignée
            Long enqueteurId,      // filtrer par enquêteur
            String search,
            String type,
            Integer priorite,
            Boolean urgent,
            Pageable pageable) {

        QEnquete enquete = QEnquete.enquete;
        QDemandeEnquete demande = QDemandeEnquete.demandeEnquete;
        QConcerne concerne = QConcerne.concerne;

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

        if(priorite != null){
            predicate.and(enquete.demandeEnquete.priorite.eq(priorite));
        }

        if(urgent!=null){
            predicate.and(enquete.demandeEnquete.urgent.eq(urgent));
        }

        if (enqueteurId != null) {
            predicate.and(enquete.enqueteur.id.eq(enqueteurId));
        }

        if(type != null){
            predicate.and(enquete.demandeEnquete.concerne.type.eq(TypeConcerne.fromValue(type)));
        }

        if (search != null && !search.isEmpty()) {
            BooleanBuilder searchPredicate = new BooleanBuilder();

            searchPredicate.or(enquete.reference.containsIgnoreCase(search));
            searchPredicate.or(enquete.instruction.containsIgnoreCase(search));

            // champs liés dans DemandeEnquete
            searchPredicate.or(enquete.demandeEnquete.reference.containsIgnoreCase(search));
            searchPredicate.or(enquete.demandeEnquete.objet.containsIgnoreCase(search));
            searchPredicate.or(enquete.demandeEnquete.description.containsIgnoreCase(search));

            // champs liés dans Concerne (via DemandeEnquete)
            searchPredicate.or(enquete.demandeEnquete.concerne.telephone.containsIgnoreCase(search));
            searchPredicate.or(enquete.demandeEnquete.concerne.regionSocial.containsIgnoreCase(search));

            predicate.and(searchPredicate);
        }

        return findAll(predicate, pageable);
    }

}
