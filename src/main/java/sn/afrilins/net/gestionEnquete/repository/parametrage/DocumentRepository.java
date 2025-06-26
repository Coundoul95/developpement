package sn.afrilins.net.gestionEnquete.repository.parametrage;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Document;

public interface DocumentRepository extends JpaRepository<Document, Long>, QuerydslPredicateExecutor<Document> {

    default Page<Document> findAllDocument(Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        return findAll(booleanBuilder, pageable);
    }
}
