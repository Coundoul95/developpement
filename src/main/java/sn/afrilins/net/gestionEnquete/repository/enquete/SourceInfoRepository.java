package sn.afrilins.net.gestionEnquete.repository.enquete;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
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
     * @param utilisateurId     Identifiant de l'utilisateur associé à la source.
     * @param nom               Nom (ou une partie du nom) de la source.
     * @param niveauFiabilite   Niveau de fiabilité de la source (filtrage partiel, insensible à la casse).
     * @param etat              Le code de l'état de la source.
     * @param type              Le code du type de la source.
     * @param search            Terme de recherche global appliqué sur plusieurs champs (nom, fiabilité, commentaires, description).
     * @param pageable          Objet de pagination et de tri.
     * @return                  Page de résultats correspondant aux critères spécifiés.
     */
    default Page<SourceInfo> readAllSourceInfo(
            Long utilisateurId,
            String nom,
            String niveauFiabilite,
            String etat,
            String type,
            String search,
            Pageable pageable) {

        QSourceInfo source = QSourceInfo.sourceInfo;
        BooleanBuilder builder = new BooleanBuilder();

        if (utilisateurId != null) {
            builder.and(source.utilisateur.id.eq(utilisateurId));
        }

        if (etat != null) {
            builder.and(source.etat.code.eq(etat));
        }

        if (type != null) {
            builder.and(source.type.code.eq(type));
        }


        if (nom != null && !nom.isBlank()) {
            builder.and(source.nom.containsIgnoreCase(nom));
        }

        if (niveauFiabilite != null && !niveauFiabilite.isBlank()) {
            builder.and(source.niveauFiabilite.containsIgnoreCase(niveauFiabilite));
        }

        if (search != null && !search.isBlank()) {
            builder.andAnyOf(
                    source.nom.containsIgnoreCase(search),
                    source.niveauFiabilite.containsIgnoreCase(search),
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