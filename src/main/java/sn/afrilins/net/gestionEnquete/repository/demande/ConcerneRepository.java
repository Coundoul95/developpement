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
    Optional<Concerne> findByTelephone(String telephone);

    default Page<Concerne> findAllConcerne(TypeConcerne type, String telephone, String regionSocial, Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        var qConcerne = QConcerne.concerne;
        BooleanBuilder builder = new BooleanBuilder();
        if (type != null && !type.toString().isEmpty()) {
            builder.and(qConcerne.type.eq(type));
        }
        if (telephone != null && !telephone.trim().isEmpty()) {
            builder.and(qConcerne.telephone.containsIgnoreCase(telephone));
        }
        if (regionSocial != null && !regionSocial.trim().isEmpty()) {
            builder.and(qConcerne.regionSocial.containsIgnoreCase(regionSocial));
        }
        return findAll(booleanBuilder, pageable);
    }
}
