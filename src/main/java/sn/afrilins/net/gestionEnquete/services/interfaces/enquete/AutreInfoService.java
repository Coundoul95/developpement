package sn.afrilins.net.gestionEnquete.services.interfaces.enquete;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.autre_info.request.AutreInfoUpdateRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.autre_info.response.AutreInfoDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.autre_info.request.AutreInfoRequestDTO;

public interface AutreInfoService {

    /**
     * Crée une nouvelle autre info.
     *
     * @param autreInfo les données de l'autre info à créer
     * @return l'autre info créée
     */
    AutreInfoDTO createAutreInfo(AutreInfoRequestDTO autreInfo);

    /**
     * Met à jour une autre info existante.
     *
     * @param autreInfo les nouvelles données de l'autre info
     * @return l'autre info mise à jour
     */
    AutreInfoDTO updateAutreInfo(Long id, AutreInfoUpdateRequestDTO autreInfo);

    /**
     * Supprime une autre info par son identifiant.
     *
     * @param id l’identifiant de l'autre info à supprimer
     */
    void deleteAutreInfo(Long id);

    /**
     * Recherche une autre info par son identifiant.
     *
     * @param id l’identifiant de l'autre info
     * @return l'autre info trouvée
     */
    AutreInfoDTO findAutreInfoById(Long id);

    /**
     * Récupère une page d'autres infos.
     *
     * @param categorie   la catégorie à filtrer (optionnel)
     * @param importance  l'importance à filtrer (optionnel)
     * @param description le contenu ou mot-clé à filtrer (optionnel)
     * @param pageable    les informations de pagination
     * @return une page d'autres infos
     */
    Page<AutreInfoDTO> readAllAutreInfos(String categorie, String objet, String description, Integer importance, String codeEtat, String search, Pageable pageable);
}
