package sn.afrilins.net.gestionEnquete.services.interfaces.demande;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.afrilins.net.gestionEnquete.services.dto.demande.EtatDemandeDTO;
import sn.afrilins.net.gestionEnquete.services.dto.demande.request.EtatDemandeRequestDTO;

/**
 * Interface pour la gestion des opérations liées aux états de demande.
 */
public interface EtatDemandeService {

    /**
     * Crée un nouvel état de demande.
     *
     * @param etatDemande les données de l'état de demande à créer
     * @return l'état de demande créé
     */
    EtatDemandeDTO createEtatDemande(EtatDemandeRequestDTO etatDemande);

    /**
     * Met à jour un état de demande existant.
     *
     * @param etatDemande les nouvelles données de l'état de demande
     * @return l'état de demande mis à jour
     */
    EtatDemandeDTO updateEtatDemande(EtatDemandeDTO etatDemande);

    /**
     * Supprime un état de demande à partir de son identifiant.
     *
     * @param id l'identifiant de l'état de demande à supprimer
     */
    void deleteEtatDemande(Long id);

    /**
     * Recherche un état de demande par son identifiant.
     *
     * @param id l'identifiant de l'état de demande
     * @return l'état de demande trouvé
     */
    EtatDemandeDTO findEtatDemandeById(Long id);

    /**
     * Récupère une page paginée des états de demande.
     *
     * @param pageable les informations de pagination
     * @return une page d'états de demande
     */
    Page<EtatDemandeDTO> readAllEtatDemandes(Pageable pageable);
}
