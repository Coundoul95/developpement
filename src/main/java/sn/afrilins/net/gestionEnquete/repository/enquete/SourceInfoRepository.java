package sn.afrilins.net.gestionEnquete.repository.enquete;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.enquete.QEnquete;
import sn.afrilins.net.gestionEnquete.domain.enquete.QSourceInfo;
import sn.afrilins.net.gestionEnquete.domain.enquete.SourceInfo;

import java.util.Optional;

/**
 * Repository JPA pour l'entité {@link SourceInfo}.
 * Fournit des méthodes CRUD et des recherches avancées avec QueryDSL.
 */
public interface SourceInfoRepository extends JpaRepository<SourceInfo, Long>, QuerydslPredicateExecutor<SourceInfo> {

    /**
     * Recherche paginée des sources d'information selon plusieurs critères optionnels.
     *
     * @param utilisateurId Identifiant de l'utilisateur associé à la source.
     * @param nom           Nom (ou une partie du nom) de la source.
     * @param fiabilite     Niveau de fiabilité de la source (filtrage partiel, insensible à la casse).
     * @param codeEtat      Le code de l'état de la source.
     * @param codeType      Le code du type de la source.
     * @param search        Terme de recherche global appliqué sur plusieurs champs (nom, fiabilité, commentaires, description).
     * @param pageable      Objet de pagination et de tri.
     * @return Page de résultats correspondant aux critères spécifiés.
     */
    default Page<SourceInfo> readAllSourceInfo(
            Long utilisateurId,
            String nom,
            Integer fiabilite,
            String codeEtat,
            String codeType,
            Long enqueteId,
            boolean excludeEnquete,
            String search,
            Pageable pageable) {

        QSourceInfo source = QSourceInfo.sourceInfo;
        QEnquete enquete = QEnquete.enquete;
        BooleanBuilder builder = new BooleanBuilder();

        if (utilisateurId != null) {
            builder.and(source.utilisateur.id.eq(utilisateurId));
        }

        if (codeEtat != null) {
            builder.and(source.etat.code.eq(codeEtat));
        }

        if (codeType != null) {
            builder.and(source.type.code.eq(codeType));
        }

        if (nom != null && !nom.isBlank()) {
            builder.and(source.nom.containsIgnoreCase(nom));
        }

        if (fiabilite != null) {
            builder.and(source.fiabilite.eq(fiabilite));
        }

        // 🔹 Gestion enquêtes
        if (enqueteId != null) {
            if (excludeEnquete) {
//                // tous les documents qui ne sont liés à aucune enquête
//                builder.and(source.enquetes.isEmpty());
                // sources qui ne sont PAS liées à cette enquête précise
                builder.and(
                        JPAExpressions.selectOne()
                                .from(enquete)
                                .where(enquete.id.eq(enqueteId)
                                        .and(enquete.in(source.enquetes)))
                                .notExists()
                );
            } else {
                // tous les documents liés à cette enquête précise
                builder.and(source.enquetes.any().id.eq(enqueteId));
            }
        }

        if (search != null && !search.isBlank()) {
            builder.andAnyOf(
                    source.nom.containsIgnoreCase(search),
                    source.commentaires.containsIgnoreCase(search),
                    source.description.containsIgnoreCase(search)
            );
        }

        return findAll(builder, pageable);
    }

    /**
     * Récupère une source d'information avec ses documents associés.
     *
     * @param id identifiant de la source
     * @return SourceInfo avec ses documents
     */
    @EntityGraph(attributePaths = {"documents"})
    Optional<SourceInfo> findById(Long id);

    /**
     * Compte le nombre de sources associées à un utilisateur donné.
     *
     * @param utilisateurId identifiant de l'utilisateur
     * @return nombre de {@link SourceInfo} associés
     */
    long countByUtilisateurId(Long utilisateurId);

    /**
     * Supprime toutes les sources associées à un utilisateur donné.
     *
     * @param utilisateurId identifiant de l'utilisateur
     */
    void deleteByUtilisateurId(Long utilisateurId);
}