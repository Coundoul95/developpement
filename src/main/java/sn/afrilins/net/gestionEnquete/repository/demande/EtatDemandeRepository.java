package sn.afrilins.net.gestionEnquete.repository.demande;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.demande.EtatDemande;

import java.util.Optional;

public interface EtatDemandeRepository extends JpaRepository<EtatDemande,Long>, QuerydslPredicateExecutor<EtatDemande> {

    Optional<EtatDemande> findFirstByCode(String code);

    Optional<EtatDemande> findFirstByLibelle(String libelle);


    default Page<EtatDemande> findAllEtatDemande(Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        return findAll(booleanBuilder, pageable);
    }
}