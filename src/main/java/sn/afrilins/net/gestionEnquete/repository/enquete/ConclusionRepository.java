package sn.afrilins.net.gestionEnquete.repository.enquete;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.enquete.AutreInfo;
import sn.afrilins.net.gestionEnquete.domain.enquete.Conclusion;
import sn.afrilins.net.gestionEnquete.domain.enquete.QAutreInfo;
import sn.afrilins.net.gestionEnquete.domain.enquete.QConclusion;

import java.util.Optional;


public interface ConclusionRepository extends JpaRepository<Conclusion, Long>, QuerydslPredicateExecutor<Conclusion> {


    default Page<Conclusion> findAllConclusion(String titre,String contenu, String recommandation, String mesuresSuivi, Pageable pageable){
        QConclusion qConclusion = QConclusion.conclusion;
        BooleanBuilder builder = new BooleanBuilder();

        if (titre != null && !titre.isBlank()) {
            builder.and(qConclusion.titre.containsIgnoreCase(titre));
        }

        if (contenu != null && !contenu.isBlank()) {
            builder.and(qConclusion.contenu.containsIgnoreCase(contenu));
        }

        if (recommandation != null && !recommandation.isBlank()) {
            builder.and(qConclusion.recommandation.containsIgnoreCase(recommandation));
        }

        if (mesuresSuivi != null && !mesuresSuivi.isBlank()) {
            builder.and(qConclusion.mesuresSuivi.containsIgnoreCase(mesuresSuivi));
        }

        return findAll(builder, pageable);
    }

    Optional<Conclusion> findFirstByTitre(String titre);
}
