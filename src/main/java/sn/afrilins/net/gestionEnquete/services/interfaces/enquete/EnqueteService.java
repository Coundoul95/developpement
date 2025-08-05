package sn.afrilins.net.gestionEnquete.services.interfaces.enquete;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.EnqueteDTO;

public interface EnqueteService {

    /**
     * Crée une nouvelle enquete.
     *
     * @param dto les données de la conclusion à créer
     * @return la conclusion créée
     */
    EnqueteDTO createEnquete(EnqueteDTO dto);

    /**
     * Met à jour une enquête existante.
     *
     * @param dto les nouvelles données de l'enquête
     * @return l'enquête mise à jour
     */
    EnqueteDTO updateEnquete(EnqueteDTO dto);

    /**
     * Supprime une enquête par son identifiant.
     *
     * @param id l’identifiant de l'enquête à supprimer
     */
    void deleteEnquete(Long id);

    /**
     * Recherche une enquête par son identifiant.
     *
     * @param id l’identifiant de la conclusion
     * @return la enquete trouvée
     */
    EnqueteDTO findEnqueteById(Long id);

    /**
     * Récupère une page de conclusions.
     *
     * @param pageable les informations de pagination
     * @return une page de conclusions
     */
    Page<EnqueteDTO> readAllEnquete(Pageable pageable);

}
