package sn.afrilins.net.gestionEnquete.services.interfaces.enquete;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.ConclusionDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.request.ConclusionRequestDTO;

public interface ConclusionService {

    /**
     * Crée une nouvelle conclusion.
     *
     * @param conclusion les données de la conclusion à créer
     * @return la conclusion créée
     */
    ConclusionDTO createConclusion(ConclusionRequestDTO conclusion);

    /**
     * Met à jour une conclusion existante.
     *
     * @param conclusion les nouvelles données de la conclusion
     * @return la conclusion mise à jour
     */
    ConclusionDTO updateConclusion(ConclusionDTO conclusion);

    /**
     * Supprime une conclusion par son identifiant.
     *
     * @param id l’identifiant de la conclusion à supprimer
     */
    void deleteConclusion(Long id);

    /**
     * Recherche une conclusion par son identifiant.
     *
     * @param id l’identifiant de la conclusion
     * @return la conclusion trouvée
     */
    ConclusionDTO findConclusionById(Long id);

    /**
     * Récupère une page de conclusions.
     *
     * @param titre le titre à filtrer (optionnel)
     * @param recommandation la recommandation à filtrer (optionnel)
     * @param contenu le contenur à filtrer (optionnel)
     * @param  mesuresSuivi la mesure à suivre à filtrer (optionnel)
     * @param pageable les informations de pagination
     * @return une page de conclusions
     */
    Page<ConclusionDTO> readAllConclusions(String titre, String contenu, String recommandation, String mesuresSuivi, Pageable pageable);

}
