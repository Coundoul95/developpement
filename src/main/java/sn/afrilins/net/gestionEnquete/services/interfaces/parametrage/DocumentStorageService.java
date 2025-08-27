package sn.afrilins.net.gestionEnquete.services.interfaces.parametrage;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.DocumentDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.DocumentDebugInfo;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.DocumentViewUrlDTO;

/**
 * Interface définissant les opérations liées à la gestion physique et logique
 * des fichiers de documents dans le système.
 */
public interface DocumentStorageService {

    /**
     * Gère l'upload d'un fichier document ainsi que la création de ses métadonnées.
     *
     * @param file        Le fichier à stocker.
     * @param nom         Le nom du document.
     * @param description La description du document (optionnelle).
     * @param typeId      L'identifiant du type de document.
     * @return Un objet {@link DocumentDTO} contenant les informations du document enregistré.
     */
    DocumentDTO handleUpload(MultipartFile file, String nom, String description, Long typeId, Long utilisateurId);

    DocumentDTO handleUpload(MultipartFile file, String nom, String description, String codeType, Long utilisateurId);

    /**
     * Récupère la ressource (fichier) correspondant à un document.
     *
     * @param documentId      L'identifiant du document.
     * @param checkIfReadable Si vrai, vérifie également la lisibilité du fichier.
     * @return La ressource {@link Resource} représentant le fichier du document.
     */
    Resource getDocumentFile(Long documentId, boolean checkIfReadable);

    /**
     * Construit les URLs permettant de visualiser et télécharger un document.
     *
     * @param document L'objet {@link DocumentDTO} du document concerné.
     * @return Un objet {@link DocumentViewUrlDTO} contenant les URLs et le type MIME du document.
     */
    DocumentViewUrlDTO buildViewUrls(DocumentDTO document);

    /**
     * Génère des informations de diagnostic (debug) pour un document,
     * comme la présence du fichier, sa lisibilité, sa taille, etc.
     *
     * @param document L'objet {@link DocumentDTO} représentant le document.
     * @param resource La ressource associée au fichier.
     * @return Un objet {@link DocumentDebugInfo} contenant les détails techniques du fichier.
     */
    DocumentDebugInfo buildDebugInfo(DocumentDTO document, Resource resource);

    /**
     * Supprime physiquement le fichier associé à un document ainsi que ses métadonnées.
     *
     * @param documentId L'identifiant du document à supprimer.
     */
    void deleteDocument(Long documentId);

}
