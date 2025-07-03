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

    default Page<AutreInfo> findAllAutreInfo(String categorie, String description, String importance, Pageable pageable){
        var qAutreInfo = QAutreInfo.autreInfo;
        BooleanBuilder builder = new BooleanBuilder();

        if (categorie != null && !categorie.isBlank()) {
            builder.and(qAutreInfo.categorie.containsIgnoreCase(categorie));
        }

        if (description != null && !description.isBlank()) {
            builder.and(qAutreInfo.description.containsIgnoreCase(description));
        }

        if (importance != null && !importance.isBlank()) {
            builder.and(qAutreInfo.importance.containsIgnoreCase(importance));
        }
        return findAll(builder, pageable);
    }
}
