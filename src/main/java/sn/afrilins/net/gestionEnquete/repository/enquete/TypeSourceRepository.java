package sn.afrilins.net.gestionEnquete.repository.enquete;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.enquete.QTypeSource;
import sn.afrilins.net.gestionEnquete.domain.enquete.TypeSource;

import java.util.Optional;

/**
 * Repository JPA pour l'entité {@link TypeSource}.
 * Fournit des opérations CRUD ainsi que des requêtes dynamiques avec QueryDSL.
 */
public interface TypeSourceRepository extends JpaRepository<TypeSource, Long>, QuerydslPredicateExecutor<TypeSource> {

    /**
     * Recherche un premier {@link TypeSource} selon son code exact.
     *
     * @param code le code à rechercher
     * @return un {@link Optional} contenant le premier résultat correspondant, ou vide si non trouvé
     */
    Optional<TypeSource> findFirstByCode(String code);

    /**
     * Recherche un premier {@link TypeSource} selon son libellé exact.
     *
     * @param libelle le libellé à rechercher
     * @return un {@link Optional} contenant le premier résultat correspondant, ou vide si non trouvé
     */
    Optional<TypeSource> findFirstByLibelle(String libelle);

    /**
     * Recherche paginée de {@link TypeSource} selon des critères optionnels.
     *
     * Filtre par code (exact, insensible à la casse) et/ou libellé (partiel, insensible à la casse).
     *
     * @param code     code à filtrer (optionnel)
     * @param libelle  libellé à filtrer (optionnel)
     * @param pageable paramètres de pagination et tri
     * @return page des {@link TypeSource} correspondant aux critères
     */
    default Page<TypeSource> findAllTypeSource(String code, String libelle, Pageable pageable) {
        var qTypeSource = QTypeSource.typeSource;
        BooleanBuilder builder = new BooleanBuilder();
        if (code != null && !code.trim().isEmpty()) {
            builder.and(qTypeSource.code.equalsIgnoreCase(code));
        }
        if (libelle != null && !libelle.trim().isEmpty()) {
            builder.and(qTypeSource.libelle.containsIgnoreCase(libelle));
        }
        return findAll(builder, pageable);
    }
}