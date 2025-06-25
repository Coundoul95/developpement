package sn.afrilins.net.gestionEnquete.services.interfaces.demande;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.afrilins.net.gestionEnquete.services.dto.demande.EtatDemandeDTO;
import sn.afrilins.net.gestionEnquete.services.dto.demande.request.EtatDemandeRequestDTO;

public interface EtatDemandeService {

    EtatDemandeDTO createEtatDemande(EtatDemandeRequestDTO EtatDemande);

    EtatDemandeDTO updateEtatDemande(EtatDemandeDTO EtatDemande);

    void deleteEtatDemande(Long id);

    EtatDemandeDTO findEtatDemandeById(Long id);

    Page<EtatDemandeDTO> readAllEtatDemandes(Pageable pageable);
}
