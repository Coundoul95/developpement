package sn.afrilins.net.gestionEnquete.services.interfaces.parametrage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.document.response.TypeDocumentDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.document.request.TypeDocumentRequestDTO;

/**
 * Interface pour la gestion des types de documents utilisés dans le système.
 */
public interface TypeDocumentService {

    /**
     * Crée un nouveau type de document.
     *
     * @param typeDocument les données du type de document à créer
     * @return le type de document créé
     */
    TypeDocumentDTO createTypeDocument(TypeDocumentRequestDTO typeDocument);

    /**
     * Met à jour un type de document existant.
     *
     * @param typeDocument les nouvelles données du type de document
     * @return le type de document mis à jour
     */
    TypeDocumentDTO updateTypeDocument(TypeDocumentDTO typeDocument);

    /**
     * Supprime un type de document par son identifiant.
     *
     * @param id l’identifiant du type de document à supprimer
     */
    void deleteTypeDocument(Long id);

    /**
     * Recherche un type de document par son identifiant.
     *
     * @param id l’identifiant du type de document
     * @return le type de document trouvé
     */
    TypeDocumentDTO findTypeDocumentById(Long id);

    TypeDocumentDTO findTypeDocumentByCode(String code);

    /**
     * Récupère une page paginée des types de document.
     *
     * @param pageable les informations de pagination
     * @return une page de types de document
     */
    Page<TypeDocumentDTO> readAllTypeDocument(String code, String libelle, Pageable pageable);
}
