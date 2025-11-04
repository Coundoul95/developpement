package sn.afrilins.net.gestionEnquete.services.interfaces.enquete;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.calendrier.response.TypeEvenementDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.calendrier.request.TypeEvenementRequestDTO;

public interface TypeEvenementService {

    TypeEvenementDTO createTypeEvenement(TypeEvenementRequestDTO dto);

    TypeEvenementDTO updateTypeEvenement(TypeEvenementDTO dto);

    void deleteTypeEvenement(Long id);

    TypeEvenementDTO findTypeEvenementById(Long id);

    Page<TypeEvenementDTO> readAllTypeEvenements(String code, String libelle, Pageable pageable);

}
