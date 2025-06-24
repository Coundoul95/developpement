package sn.afrilins.net.gestionEnquete.repository.enquete;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.enquete.EtatEnquete;

import java.util.Optional;

public interface EtatEnqueteRepository  extends JpaRepository<EtatEnquete,Long>, QuerydslPredicateExecutor<EtatEnquete> {

    Optional<EtatEnquete> findFirstByCode(String code);

    Optional<EtatEnquete> findFirstByLibelle(String libelle);


    default Page<EtatEnquete> findAllEtatEnquete(Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        return findAll(booleanBuilder, pageable);
    }
}

