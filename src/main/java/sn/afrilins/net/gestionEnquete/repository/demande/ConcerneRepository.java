package sn.afrilins.net.gestionEnquete.repository.demande;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.demande.Concerne;
import sn.afrilins.net.gestionEnquete.domain.demande.QConcerne;
import sn.afrilins.net.gestionEnquete.domain.enume.TypeConcerne;

import java.util.Optional;

public interface ConcerneRepository extends JpaRepository<Concerne, Long>, QuerydslPredicateExecutor<Concerne> {
    Optional<Concerne> findByNumero(String numero);

    default Page<Concerne> findAllConcerne(TypeConcerne type, String numero, String regionSocial, Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        var qConcerne = QConcerne.concerne;
        BooleanBuilder builder = new BooleanBuilder();
        if (type != null && !type.toString().isEmpty()) {
            builder.and(qConcerne.type.eq(type));
        }
        if (numero != null && !numero.trim().isEmpty()) {
            builder.and(qConcerne.numero.containsIgnoreCase(numero));
        }
        if (regionSocial != null && !regionSocial.trim().isEmpty()) {
            builder.and(qConcerne.regionSocial.containsIgnoreCase(regionSocial));
        }
        return findAll(booleanBuilder, pageable);
    }
}
