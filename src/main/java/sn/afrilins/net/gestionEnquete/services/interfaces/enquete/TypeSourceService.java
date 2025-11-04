package sn.afrilins.net.gestionEnquete.services.interfaces.enquete;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.source_info.response.TypeSourceDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.source_info.request.TypeSourceRequestDTO;

/**
 * Interface pour la gestion des types de source d’information.
 */
public interface TypeSourceService {

    /**
     * Crée un nouveau type de source.
     *
     * @param typeSource les données du type de source à créer
     * @return le type de source créé
     */
    TypeSourceDTO createTypeSource(TypeSourceRequestDTO typeSource);

    /**
     * Met à jour un type de source existant.
     *
     * @param typeSource les nouvelles données du type de source
     * @return le type de source mis à jour
     */
    TypeSourceDTO updateTypeSource(TypeSourceDTO typeSource);

    /**
     * Supprime un type de source par son identifiant.
     *
     * @param id l’identifiant du type de source à supprimer
     */
    void deleteTypeSource(Long id);

    /**
     * Recherche un type de source par son identifiant.
     *
     * @param id l’identifiant du type de source
     * @return le type de source trouvé
     */
    TypeSourceDTO findTypeSourceById(Long id);

    /**
     * Récupère une page paginée de types de source.
     *
     * @param pageable les informations de pagination
     * @return une page de types de source
     */
    Page<TypeSourceDTO> readAllTypeSources(String code, String libelle, Pageable pageable);
}
