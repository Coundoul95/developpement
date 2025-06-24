package sn.afrilins.net.gestionEnquete.repository.enquete;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.enquete.TypeSource;

import java.util.Optional;

public interface TypeSourceRepository extends JpaRepository<TypeSource,Long>, QuerydslPredicateExecutor<TypeSource> {

    Optional<TypeSource> findFirstByCode(String code);

    Optional<TypeSource> findFirstByLibelle(String libelle);


    default Page<TypeSource> findAllTypeSource(Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        return findAll(booleanBuilder, pageable);
    }
}
