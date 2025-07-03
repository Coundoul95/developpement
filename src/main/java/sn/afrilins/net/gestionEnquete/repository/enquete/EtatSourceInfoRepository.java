package sn.afrilins.net.gestionEnquete.repository.enquete;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.enquete.EtatSourceInfo;
import sn.afrilins.net.gestionEnquete.domain.enquete.QEtatSourceInfo;

import java.util.Optional;


/**
 * Repository JPA pour l'entité {@link EtatSourceInfo}.
 * Fournit les opérations CRUD ainsi que des recherches avancées avec QueryDSL.
 */
public interface EtatSourceInfoRepository extends JpaRepository<EtatSourceInfo, Long>, QuerydslPredicateExecutor<EtatSourceInfo> {

    /**
     * Recherche le premier {@link EtatSourceInfo} correspondant au code donné.
     *
     * @param code le code recherché
     * @return un {@link Optional} contenant l'entité trouvée, ou vide si aucune correspondance
     */
    Optional<EtatSourceInfo> findFirstByCode(String code);

    /**
     * Recherche le premier {@link EtatSourceInfo} correspondant au libellé donné.
     *
     * @param libelle le libellé recherché
     * @return un {@link Optional} contenant l'entité trouvée, ou vide si aucune correspondance
     */
    Optional<EtatSourceInfo> findFirstByLibelle(String libelle);

    /**
     * Recherche paginée des {@link EtatSourceInfo} en fonction de critères optionnels.
     * Les critères sont combinés par un ET logique.
     *
     * @param code     (optionnel) filtre sur le code, recherche exacte, insensible à la casse
     * @param libelle  (optionnel) filtre sur le libellé, recherche partielle, insensible à la casse
     * @param pageable informations de pagination et tri
     * @return une page des résultats correspondant aux critères
     */
    default Page<EtatSourceInfo> findAllEtatSourceInfo(String code, String libelle, Pageable pageable) {
        var qEtatSourceInfo = QEtatSourceInfo.etatSourceInfo;
        BooleanBuilder builder = new BooleanBuilder();
        if (code != null && !code.trim().isEmpty()) {
            builder.and(qEtatSourceInfo.code.equalsIgnoreCase(code));
        }
        if (libelle != null && !libelle.trim().isEmpty()) {
            builder.and(qEtatSourceInfo.libelle.containsIgnoreCase(libelle));
        }
        return findAll(builder, pageable);
    }
}