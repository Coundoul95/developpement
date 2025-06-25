package sn.afrilins.net.gestionEnquete.repository.parametrage;

import com.querydsl.core.BooleanBuilder;
// import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.parametrage.TypeLocal;

import java.util.Optional;

public interface TypeLocalRepository extends JpaRepository<TypeLocal, Long>, QuerydslPredicateExecutor<TypeLocal> {

    Optional<TypeLocal> findFirstByCode(String code);

    Optional<TypeLocal> findFirstByLibelle(String libelle);

    default Page<TypeLocal> findAllTypeLocal(Pageable pageable, String code, String libelle) {
        var booleanBuilder = new BooleanBuilder();
//        if (StringUtils.isNotEmpty(code)) {
//            booleanBuilder.and(QTypeLocal.typeLocal.code.containsIgnoreCase(code));
//        }
//        if (StringUtils.isNotEmpty(libelle)) {
//            booleanBuilder.and(QTypeLocal.typeLocal.libelle.containsIgnoreCase(libelle));
//        }
        return findAll(booleanBuilder, pageable);
    }
}
