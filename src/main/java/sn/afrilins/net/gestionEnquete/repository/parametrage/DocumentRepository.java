package sn.afrilins.net.gestionEnquete.repository.parametrage;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Document;
import sn.afrilins.net.gestionEnquete.domain.parametrage.QDocument;
import sn.afrilins.net.gestionEnquete.util.AppUtils;

import java.util.List;
import java.util.Objects;

public interface DocumentRepository extends JpaRepository<Document, Long>, QuerydslPredicateExecutor<Document> {

    default Page<Document> findAllDocument(
            Pageable pageable,
            String nom,
            String extension,
            String type,
            String categorie,
            Long utilisateurId) {
        var builder = new BooleanBuilder();
        QDocument doc = QDocument.document;

        if (nom != null && !nom.isEmpty()) {
            builder.and(doc.nom.containsIgnoreCase(nom));
        }
        if (extension != null && !extension.isEmpty()) {
            builder.and(doc.extension.equalsIgnoreCase(extension));
        }
        if (type != null && !type.isEmpty()) {
            builder.and(doc.type.code.equalsIgnoreCase(type));
        }
        if (categorie != null && !categorie.isEmpty()) {
            List<String> extensions = AppUtils.FILE_EXTENSION_CATEGORY_MAP.get(categorie.toLowerCase());
            if (extensions != null) {
                builder.and(doc.extension.in(extensions));
            }
        }
        if(Objects.nonNull(utilisateurId)){
            builder.and(doc.utilisateur.id.eq(utilisateurId));
        }
        return findAll(builder, pageable);
    }

}
