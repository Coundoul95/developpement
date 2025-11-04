package sn.afrilins.net.gestionEnquete.services.interfaces.demande;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.afrilins.net.gestionEnquete.domain.enume.TypeConcerne;
import sn.afrilins.net.gestionEnquete.services.dto.demande.concerne.response.ConcerneDTO;
import sn.afrilins.net.gestionEnquete.services.dto.demande.concerne.request.ConcerneRequestDTO;


public interface ConcerneService {

    /**
     * Crée un nouvel état de demande.
     *
     * @param dto les données de l'état de demande à créer
     * @return l'état de demande créé
     */
    ConcerneDTO createConcerne(ConcerneRequestDTO dto);

    /**
     * Met à jour un état de demande existant.
     *
     * @param dto les nouvelles données de l'état de demande
     * @return l'état de demande mis à jour
     */
    ConcerneDTO updateConcerne(ConcerneDTO dto);

    /**
     * Supprime un état de demande à partir de son identifiant.
     *
     * @param id l'identifiant de l'état de demande à supprimer
     */
    void deleteConcerne(Long id);

    /**
     * Recherche un état de demande par son identifiant.
     *
     * @param id l'identifiant de l'état de demande
     * @return l'état de demande trouvé
     */
    ConcerneDTO findConcerneById(Long id);

    /**
     * Récupère une page paginée des états de demande.
     *
     * @param pageable les informations de pagination
     * @return une page d'états de demande
     */
    Page<ConcerneDTO> readAllConcernes(TypeConcerne type, String telephone, String regionSocial, Pageable pageable);
}
