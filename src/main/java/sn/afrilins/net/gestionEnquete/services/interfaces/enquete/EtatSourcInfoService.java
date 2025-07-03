package sn.afrilins.net.gestionEnquete.services.interfaces.enquete;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.EtatSourceInfoDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.request.EtatSourceInfoRequestDTO;

public interface EtatSourcInfoService {

    /**
     * Crée un nouvel état source d'information.
     *
     * @param etatSourceInfo les données de l'état à créer
     * @return l'état source d'information créé
     */
    EtatSourceInfoDTO createEtatSourceInfo(EtatSourceInfoRequestDTO etatSourceInfo);

    /**
     * Met à jour un état source d'information existant.
     *
     * @param etatSourceInfo les nouvelles données de l'état source d'information
     * @return l'état source d'information mis à jour
     */
    EtatSourceInfoDTO updateEtatSourceInfo(EtatSourceInfoDTO etatSourceInfo);

    /**
     * Supprime un état source d'information par son identifiant.
     *
     * @param id l'identifiant de l'état à supprimer
     */
    void deleteEtatSourceInfo(Long id);

    /**
     * Recherche un état source d'information par son identifiant.
     *
     * @param id l'identifiant de l'état source d'information
     * @return l'état trouvé
     */
    EtatSourceInfoDTO findEtatSourceInfoById(Long id);

    /**
     * Récupère une page d'états source d'information.
     *
     * @param pageable les informations de pagination
     * @param code le code de l'état (filtre optionnel)
     * @param libelle le libellé de l'état (filtre optionnel)
     * @return une page d'états source d'information
     */
    Page<EtatSourceInfoDTO> readAllEtatSourceInfos(String code, String libelle, Pageable pageable);

}
