package sn.afrilins.net.gestionEnquete.services.interfaces.enquete;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.EtatEnqueteDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.request.EtatEnqueteRequestDTO;

public interface EtatEnqueteService {

    EtatEnqueteDTO createEtatEnquete(EtatEnqueteRequestDTO EtatEnquete);

    EtatEnqueteDTO updateEtatEnquete(EtatEnqueteDTO EtatEnquete);

    void deleteEtatEnquete(Long id);

    EtatEnqueteDTO findEtatEnqueteById(Long id);

    Page<EtatEnqueteDTO> readAllEtatEnquetes(Pageable pageable);
}
