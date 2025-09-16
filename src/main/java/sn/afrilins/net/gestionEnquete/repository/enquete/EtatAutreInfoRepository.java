package sn.afrilins.net.gestionEnquete.repository.enquete;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.enquete.EtatAutreInfo;
import sn.afrilins.net.gestionEnquete.domain.enquete.QEtatEnquete;

import java.util.Optional;

public interface EtatAutreInfoRepository  extends JpaRepository<EtatAutreInfo, Long>, QuerydslPredicateExecutor<EtatAutreInfo> {

    /**
     * Recherche le premier {@link EtatAutreInfo} correspondant au code donné.
     *
     * @param code le code de l'état d'enquête recherché
     * @return un {@link Optional} contenant l'entité si trouvée, sinon vide
     */
    Optional<EtatAutreInfo> findFirstByCode(String code);

    /**
     * Recherche le premier {@link EtatAutreInfo} correspondant au libellé donné.
     *
     * @param libelle le libellé de l'état d'enquête recherché
     * @return un {@link Optional} contenant l'entité si trouvée, sinon vide
     */
    Optional<EtatAutreInfo> findFirstByLibelle(String libelle);

    /**
     * Récupère une page paginée de tous les {@link EtatAutreInfo} sans filtre spécifique.
     *
     * @param pageable les informations de pagination et tri
     * @return une page contenant les états d'enquête
     */
    default Page<EtatAutreInfo> findAllEtatEnquete(String code, String libelle, Pageable pageable) {
        var qEtatEnquete = QEtatEnquete.etatEnquete;
        BooleanBuilder builder = new BooleanBuilder();
        if (code != null && !code.trim().isEmpty()) {
            builder.and(qEtatEnquete.code.equalsIgnoreCase(code));
        }
        if (libelle != null && !libelle.trim().isEmpty()) {
            builder.and(qEtatEnquete.libelle.containsIgnoreCase(libelle));
        }
        return findAll(builder, pageable);
    }
}
