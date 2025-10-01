package sn.afrilins.net.gestionEnquete.services.interfaces.parametrage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.document.response.DocumentDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.document.response.DocumentUsageDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.document.request.DocumentRequestDTO;

public interface DocumentService {
    /**
     * Crée un nouveau document.
     *
     * @param document les données du document à créer
     * @return le document créé
     */
    DocumentDTO createDocument(DocumentRequestDTO document);

    /**
     * Met à jour un document existant.
     *
     * @param document les nouvelles données du document
     * @return le document mis à jour
     */
    DocumentDTO updateDocument(DocumentDTO document);

    /**
     * Supprime un document par son identifiant.
     *
     * @param id l’identifiant du document à supprimer
     */
//    void deleteDocument(Long id);
    boolean deleteDocument(Long id, Long enqueteId, Long demandeId, Long sourceInfoId);

    /**
     * Recherche un document par son identifiant.
     *
     * @param id l’identifiant du document
     * @return le document trouvé
     */
    DocumentDTO findDocumentById(Long id);

    /**
     * Récupère une page de documents.
     *
     * @param pageable les informations de pagination
     * @return une page de documents
     */
//    Page<DocumentDTO> readAllDocuments(Pageable pageable, String nom, String extension, String type, String categorie, Long utilisateurId);

    Page<DocumentDTO> readAllDocuments(Pageable pageable, String search, String nom,
                                       String extension, String type,
                                       String categorie, Long utilisateurId,
                                       Long enqueteId, Boolean excludeEnquete,
                                       Long demandeId, Boolean excludeDemande,
                                       Long sourceInfoId, Boolean excludeSource);

    DocumentUsageDTO checkIfUsed(Long documentId, Long enqueteId, Long demandeId, Long sourceInfoId);
}
