package sn.afrilins.net.gestionEnquete.repository.parametrage;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.parametrage.QTypeDocument;
import sn.afrilins.net.gestionEnquete.domain.parametrage.TypeDocument;

import java.util.Optional;

public interface TypeDocumentRepository extends JpaRepository<TypeDocument,Long>, QuerydslPredicateExecutor<TypeDocument> {

    Optional<TypeDocument> findFirstByCode(String code);
    
    Optional<TypeDocument> findFirstByLibelle(String libelle);


    default Page<TypeDocument> findAllTypeDocument(String code, String libelle, Pageable pageable) {
        var qTypeDocument = QTypeDocument.typeDocument;
        BooleanBuilder builder = new BooleanBuilder();
        if (code != null && !code.trim().isEmpty()) {
            builder.and(qTypeDocument.code.equalsIgnoreCase(code));
        }
        if (libelle != null && !libelle.trim().isEmpty()) {
            builder.and(qTypeDocument.libelle.containsIgnoreCase(libelle));
        }
        return findAll(builder, pageable);
    }
}
