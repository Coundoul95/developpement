package sn.afrilins.net.gestionEnquete.repository.enquete;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.enquete.QTypeEvenement;
import sn.afrilins.net.gestionEnquete.domain.enquete.TypeEvenement;

import java.util.Optional;

public interface TypeEvenementRepository extends JpaRepository<TypeEvenement, Long>, QuerydslPredicateExecutor<TypeEvenement> {

    Optional<TypeEvenement> findFirstByCode(String code);

    Optional<TypeEvenement> findFirstByLibelle(String libelle);

    default Page<TypeEvenement> findAllTypeEvenement(String code, String libelle, Pageable pageable) {
        QTypeEvenement qTypeEvenement = QTypeEvenement.typeEvenement;
        BooleanBuilder builder = new BooleanBuilder();

        if (code != null && !code.trim().isEmpty()) {
            builder.and(qTypeEvenement.code.equalsIgnoreCase(code));
        }

        if (libelle != null && !libelle.trim().isEmpty()) {
            builder.and(qTypeEvenement.libelle.containsIgnoreCase(libelle));
        }

        return findAll(builder, pageable);
    }
}
