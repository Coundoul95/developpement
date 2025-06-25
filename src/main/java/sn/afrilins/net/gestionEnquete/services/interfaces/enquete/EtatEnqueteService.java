package sn.afrilins.net.gestionEnquete.services.interfaces.enquete;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.EtatEnqueteDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.request.EtatEnqueteRequestDTO;

/**
 * Interface pour la gestion des opérations liées aux états d’enquête.
 */
public interface EtatEnqueteService {

    /**
     * Crée un nouvel état d’enquête.
     *
     * @param etatEnquete les données de l’état d’enquête à créer
     * @return l’état d’enquête créé
     */
    EtatEnqueteDTO createEtatEnquete(EtatEnqueteRequestDTO etatEnquete);

    /**
     * Met à jour un état d’enquête existant.
     *
     * @param etatEnquete les nouvelles données de l’état d’enquête
     * @return l’état d’enquête mis à jour
     */
    EtatEnqueteDTO updateEtatEnquete(EtatEnqueteDTO etatEnquete);

    /**
     * Supprime un état d’enquête par son identifiant.
     *
     * @param id l’identifiant de l’état d’enquête à supprimer
     */
    void deleteEtatEnquete(Long id);

    /**
     * Recherche un état d’enquête par son identifiant.
     *
     * @param id l’identifiant de l’état d’enquête
     * @return l’état d’enquête trouvé
     */
    EtatEnqueteDTO findEtatEnqueteById(Long id);

    /**
     * Récupère une page d’états d’enquête.
     *
     * @param pageable les informations de pagination
     * @return une page d’états d’enquête
     */
    Page<EtatEnqueteDTO> readAllEtatEnquetes(Pageable pageable);
}
