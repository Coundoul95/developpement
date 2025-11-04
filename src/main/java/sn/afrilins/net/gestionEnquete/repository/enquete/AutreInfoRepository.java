package sn.afrilins.net.gestionEnquete.repository.enquete;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.enquete.AutreInfo;
import sn.afrilins.net.gestionEnquete.domain.enquete.QAutreInfo;

import java.util.Optional;

public interface AutreInfoRepository extends JpaRepository<AutreInfo, Long>, QuerydslPredicateExecutor<AutreInfo> {

    Optional<AutreInfo> findFirstByCategorie(String categorie);

    default Page<AutreInfo> findAllAutreInfo(
            String categorie,
            String objet,
            String description,
            Integer importance,
            String codeEtat,
            String search,
            Pageable pageable) {

        var qAutreInfo = QAutreInfo.autreInfo;
        BooleanBuilder builder = new BooleanBuilder();

        if (categorie != null && !categorie.isBlank()) {
            builder.and(qAutreInfo.categorie.containsIgnoreCase(categorie));
        }

        if (objet != null && !objet.isBlank()) {
            builder.and(qAutreInfo.objet.containsIgnoreCase(objet));
        }

        if (description != null && !description.isBlank()) {
            builder.and(qAutreInfo.description.containsIgnoreCase(description));
        }

        if (importance != null) {
            builder.and(qAutreInfo.importance.eq(importance));
        }

        if (codeEtat != null && !codeEtat.isBlank()) {
            builder.and(qAutreInfo.etat.code.equalsIgnoreCase(codeEtat));
        }

        if (search != null && !search.isBlank()) {
            builder.andAnyOf(
                    qAutreInfo.categorie.containsIgnoreCase(search),
                    qAutreInfo.objet.containsIgnoreCase(search),
                    qAutreInfo.description.containsIgnoreCase(search),
                    qAutreInfo.etat.libelle.containsIgnoreCase(search)
            );
        }

        return findAll(builder, pageable);
    }
}