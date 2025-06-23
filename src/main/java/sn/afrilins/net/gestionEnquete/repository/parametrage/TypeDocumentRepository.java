package sn.afrilins.net.gestionEnquete.repository.parametrage;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.parametrage.TypeDocument;

import java.util.Optional;

public interface TypeDocumentRepository extends JpaRepository<TypeDocument,Long>, QuerydslPredicateExecutor<TypeDocument> {

    Optional<TypeDocument> findFirstByCode(String code);
    
    Optional<TypeDocument> findFirstByLibelle(String libelle);


    default Page<TypeDocument> findAllTypeDocument(Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        return findAll(booleanBuilder, pageable);
    }
}
