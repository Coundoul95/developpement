package sn.afrilins.net.gestionEnquete.controllers.parametre;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.*;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.DocumentService;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.DocumentStorageService;
import sn.afrilins.net.gestionEnquete.util.AppUtils;

import javax.validation.Valid;

@Hidden
@RestController
@RequestMapping("/v1/api/documents")
@Tag(name = "/v1/api/documents", description = "Document, controllers")
@RequiredArgsConstructor
@Slf4j
public class DocumentRessource {

    private final DocumentService documentService;
    private final DocumentStorageService documentStorageService;

    /**
     * Téléverse un fichier document avec ses métadonnées.
     */
    @Operation(summary = "Téléverser un document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document enregistré avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentDTO uploadDocument(
            @RequestPart("file") MultipartFile file,
            @RequestParam("nom") String nom,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("typeId") Long typeId,
            @RequestParam("utilisateurId") Long utilisateurId
    ) {
        return documentStorageService.handleUpload(file, nom, description, typeId, utilisateurId);
    }


    /**
     * Visualise ou télécharge un document.
     */
    @GetMapping("/{id}/view")
    @Operation(summary = "Visualiser ou télécharger un document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document retourné avec succès"),
            @ApiResponse(responseCode = "404", description = "Document non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Resource> viewDocument(
            @PathVariable Long id,
            @RequestParam(value = "download", defaultValue = "false") boolean download
    ) {
        try {
            DocumentDTO document = documentService.findDocumentById(id);
            Resource resource = documentStorageService.getDocumentFile(id, true);
            String contentType = AppUtils.getContentTypeByExtension(document.getExtension());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            String dispositionType = download ? "attachment" : "inline";
            headers.setContentDispositionFormData(dispositionType, document.getNom() + "." + document.getExtension());
            headers.setCacheControl("max-age=3600");
            headers.add("Permissions-Policy", "fullscreen=(self)");
            return ResponseEntity.ok().headers(headers).body(resource);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération du document ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupère les URLs de visualisation et de téléchargement d’un document.
     */
    @GetMapping("/{id}/url")
    @Operation(summary = "Obtenir les URLs d'accès à un document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URLs générées avec succès"),
            @ApiResponse(responseCode = "404", description = "Document non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<DocumentViewUrlDTO> getDocumentViewUrl(@PathVariable Long id) {
        try {
            DocumentDTO document = documentService.findDocumentById(id);
            DocumentViewUrlDTO viewUrlDTO = documentStorageService.buildViewUrls(document);
            return ResponseEntity.ok(viewUrlDTO);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération de l'URL pour le document ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Vérifie l'existence et la lisibilité du fichier physique lié à un document.
     */
    @GetMapping("/{id}/debug")
    @Operation(summary = "Diagnostiquer un document (vérification du fichier)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informations de diagnostic retournées avec succès"),
            @ApiResponse(responseCode = "404", description = "Document non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<DocumentDebugInfo> debugDocument(@PathVariable Long id) {
        try {
            DocumentDTO document = documentService.findDocumentById(id);
            Resource resource = documentStorageService.getDocumentFile(id, false);
            DocumentDebugInfo debugInfo = documentStorageService.buildDebugInfo(document, resource);
            return ResponseEntity.ok(debugInfo);
        } catch (Exception e) {
            log.error("Erreur lors du diagnostic du document ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Met à jour les métadonnées d’un document.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour les métadonnées d’un document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document mis à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Document non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public DocumentDTO updateDocumentMetadata(
            @PathVariable Long id,
            @Valid @RequestBody DocumentDTO documentDTO
    ) {
        documentDTO.setId(id);
        return documentService.updateDocument(documentDTO);
    }

    /**
     * Retourne la liste paginée des documents.
     */
    @GetMapping("/all")
    @Operation(summary = "Lister les documents (paginé)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des documents retournée avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public Page<DocumentDTO> readAllDocuments(
            Pageable pageable,
            @Parameter(description = "le nom du document")   @RequestParam(required = false) String nom,
            @Parameter(description = "l'extention du document")  @RequestParam(required = false) String extension,
            @Parameter(description = "le type de document") @RequestParam(required = false) String type,
            @Parameter(description = "L'identifiant de l'utilisateur") @RequestParam(required = false)Long utilisateurId,
            @Parameter(description = "la catégorie d'extenstion: image, document, code...", example = "document") @RequestParam(required = false) String categorie
    ) {
        return documentService.readAllDocuments(pageable, nom, extension, type, categorie, utilisateurId);
    }

    /**
     * Récupère les informations d’un document à partir de son ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer les détails d’un document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document trouvé"),
            @ApiResponse(responseCode = "404", description = "Document non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public DocumentDTO findDocumentById(@PathVariable Long id) {
        return documentService.findDocumentById(id);
    }

    /**
     * Supprime un document par son identifiant.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Document non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDocument(@PathVariable Long id) {
        documentStorageService.deleteDocument(id);
        documentService.deleteDocument(id);
    }
}
