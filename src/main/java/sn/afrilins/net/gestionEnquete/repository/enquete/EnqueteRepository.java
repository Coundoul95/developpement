package sn.afrilins.net.gestionEnquete.repository.enquete;

import com.querydsl.core.BooleanBuilder;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.enquete.Enquete;
import sn.afrilins.net.gestionEnquete.domain.enquete.QEnquete;
import sn.afrilins.net.gestionEnquete.services.dto.demande.demande_enquete.response.DemandeValideEnqueteStatsDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface EnqueteRepository extends JpaRepository<Enquete, Long>, QuerydslPredicateExecutor<Enquete> {
//
//    @Query("SELECT COUNT(e), " +
//            "SUM(CASE WHEN d.urgent = true OR d.priorite = 1 THEN 1 ELSE 0 END), " +
//            "SUM(CASE WHEN e.etat.code = '00' THEN 1 ELSE 0 END), " +
//            "SUM(CASE WHEN d.dateEcheance < CURRENT_TIMESTAMP AND e.etat.code <> 'TERMINE' THEN 1 ELSE 0 END) " +
//            "FROM Enquete e " +
//            "JOIN e.demandeEnquete d " +
//            "WHERE d.etat.code = '01' " + // uniquement demandes validées
//            "AND (:utilisateurId IS NULL OR d.utilisateur.id = :utilisateurId)"
//    )
//    Object[] getStatsForValidatedDemandes(@Param("utilisateurId") Long utilisateurId);

//    @Query("SELECT COUNT(e), " +
//            "SUM(CASE WHEN d.urgent = true OR d.priorite = 1 THEN 1 ELSE 0 END), " +
//            "SUM(CASE WHEN e.etat.code = '00' THEN 1 ELSE 0 END), " +
//            "SUM(CASE WHEN d.dateEcheance < CURRENT_TIMESTAMP AND e.etat.code <> 'TERMINE' THEN 1 ELSE 0 END) " +
//            "FROM Enquete e " +
//            "JOIN e.demandeEnquete d " +
//            "WHERE d.etat.code = '01' " +
//            "AND (:utilisateurId IS NULL OR d.utilisateur.id = :utilisateurId)")
//    Object[] getStatsForValidatedDemandes(@Param("utilisateurId") Long utilisateurId);

//    @Query("SELECT COUNT(e), " +
//            "SUM(CASE WHEN d.urgent = true OR d.priorite = 1 THEN 1 ELSE 0 END), " +
//            "SUM(CASE WHEN e.etat.code = '00' THEN 1 ELSE 0 END), " +
//            "SUM(CASE WHEN d.dateEcheance < CURRENT_TIMESTAMP AND e.etat.code <> 'TERMINE' THEN 1 ELSE 0 END) " +
//            "FROM Enquete e " +
//            "JOIN e.demandeEnquete d " +
//            "WHERE d.etat.code = '01' " +
//            "AND (:utilisateurId IS NULL OR d.utilisateur.id = :utilisateurId)")
//    Object[] getStatsForValidatedDemandes(@Param("utilisateurId") Long utilisateurId);
//

//    @Query("SELECT new sn.afrilins.net.gestionEnquete.dto.DemandeValideEnqueteStatsDTO(" +
//            "COUNT(e), " +
//            "SUM(CASE WHEN d.urgent = true OR d.priorite = 1 THEN 1 ELSE 0 END), " +
//            "SUM(CASE WHEN e.etat.code = '00' THEN 1 ELSE 0 END), " +
//            "SUM(CASE WHEN d.dateEcheance < CURRENT_TIMESTAMP AND e.etat.code <> 'TERMINE' THEN 1 ELSE 0 END)) " +
//            "FROM Enquete e " +
//            "JOIN e.demandeEnquete d " +
//            "WHERE d.etat.code = '01' " +
//            "AND (:utilisateurId IS NULL OR d.utilisateur.id = :utilisateurId)")
//    DemandeValideEnqueteStatsDTO getStatsForValidatedDemandes(@Param("utilisateurId") Long utilisateurId);


//    @Query("SELECT COUNT(e), " +
//            "SUM(CASE WHEN d.urgent = true OR d.priorite = 1 THEN 1 ELSE 0 END), " +
//            "SUM(CASE WHEN e.etat.code = '00' THEN 1 ELSE 0 END), " +
//            "SUM(CASE WHEN d.dateEcheance < CURRENT_TIMESTAMP AND e.etat.code <> 'TERMINE' THEN 1 ELSE 0 END) " +
//            "FROM Enquete e " +
//            "JOIN e.demandeEnquete d " +
//            "WHERE d.etat.code = '01' " +
//            "AND (:utilisateurId IS NULL OR d.utilisateur.id = :utilisateurId)")
//    List<Object[]> getStatsForValidatedDemandes(@Param("utilisateurId") Long utilisateurId);

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
