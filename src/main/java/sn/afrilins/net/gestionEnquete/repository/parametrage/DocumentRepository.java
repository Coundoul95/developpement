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

//    default Page<Document> findAllDocument(
//            Pageable pageable,
//            String nom,
//            String extension,
//            String type,
//            String categorie,
//            Long utilisateurId) {
//        var builder = new BooleanBuilder();
//        QDocument doc = QDocument.document;
//
//        if (nom != null && !nom.isEmpty()) {
//            builder.and(doc.nom.containsIgnoreCase(nom));
//        }
//        if (extension != null && !extension.isEmpty()) {
//            builder.and(doc.extension.equalsIgnoreCase(extension));
//        }
//        if (type != null && !type.isEmpty()) {
//            builder.and(doc.type.code.equalsIgnoreCase(type));
//        }
//        if (categorie != null && !categorie.isEmpty()) {
//            List<String> extensions = AppUtils.FILE_EXTENSION_CATEGORY_MAP.get(categorie.toLowerCase());
//            if (extensions != null) {
//                builder.and(doc.extension.in(extensions));
//            }
//        }
//        if(Objects.nonNull(utilisateurId)){
//            builder.and(doc.utilisateur.id.eq(utilisateurId));
//        }
//        return findAll(builder, pageable);
//    }

    default Page<Document> findAllDocument(
            Pageable pageable,
            String search,
            String nom,
            String extension,
            String type,
            String categorie,
            Long utilisateurId,
            Long enqueteId,
            boolean excludeEnquete,
            Long demandeId,
            boolean excludeDemande,
            Long sourceInfoId,
            boolean excludeSource
    ) {
        QDocument doc = QDocument.document;
        var builder = new BooleanBuilder();

        // 🔍 Recherche globale
        if (search != null && !search.isEmpty()) {
            builder.andAnyOf(
                    doc.nom.containsIgnoreCase(search),
                    doc.description.containsIgnoreCase(search),
                    doc.type.code.containsIgnoreCase(search),
                    doc.extension.containsIgnoreCase(search)
            );
        }

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
        if (utilisateurId != null) {
            builder.and(doc.utilisateur.id.eq(utilisateurId));
        }

        // 🔹 Gestion enquêtes
//        if (enqueteId != null) {
//            if (excludeEnquete) {
//                // tous les documents qui ne sont liés à aucune enquête
//                builder.and(doc.enquetes.isEmpty());
//            } else {
//                // tous les documents liés à cette enquête précise
//                builder.and(doc.enquetes.any().id.eq(enqueteId));
//            }
//        }
        // 🔹 Gestion Enquêtes
        if (excludeEnquete) {
            if (enqueteId != null) {
                // Exclure les documents liés à CETTE enquête
                builder.and(doc.enquetes.any().id.eq(enqueteId).not());
            } else {
                // Exclure tous les documents ayant une enquête
                builder.and(doc.enquetes.isEmpty());
            }
        } else if (enqueteId != null) {
            // Inclure les documents liés à CETTE enquête
            builder.and(doc.enquetes.any().id.eq(enqueteId));
        }

        // 🔹 Gestion Demandes
        if (excludeDemande) {
            if (demandeId != null) {
                // Exclure les documents liés à CETTE demande
                builder.and(doc.demandesEnquete.any().id.eq(demandeId).not());
            } else {
                // Exclure tous les documents ayant une demande
                builder.and(doc.demandesEnquete.isEmpty());
            }
        } else if (demandeId != null) {
            // Inclure les documents liés à CETTE demande
            builder.and(doc.demandesEnquete.any().id.eq(demandeId));
        }

        // 🔹 Gestion Sources
        if (excludeSource) {
            if (sourceInfoId != null) {
                // Exclure les documents liés à CETTE source
                builder.and(doc.sourceInfos.any().id.eq(sourceInfoId).not());
            } else {
                // Exclure tous les documents ayant une source
                builder.and(doc.sourceInfos.isEmpty());
            }
        } else if (sourceInfoId != null) {
            // Inclure les documents liés à CETTE source
            builder.and(doc.sourceInfos.any().id.eq(sourceInfoId));
        }

        return findAll(builder, pageable);
    }


}
