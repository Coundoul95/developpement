package sn.afrilins.net.gestionEnquete.controllers.demande;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import sn.afrilins.net.gestionEnquete.domain.enume.TypeConcerne;
import sn.afrilins.net.gestionEnquete.services.dto.demande.DemandeEnqueteDTO;
import sn.afrilins.net.gestionEnquete.services.dto.demande.request.ConcerneRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.demande.request.DemandeEnqueteRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.demande.request.DemandeEnqueteUpdateRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.demande.DemandeEnqueteService;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/v1/api/demande/enquete")
@Tag(name = "/v1/api/demande/enquete", description = "DemandeEnquete, controllers")
@RequiredArgsConstructor
@Slf4j
public class DemandeEnqueteRessource {

    private final DemandeEnqueteService demandeEnqueteService;

    @Operation(summary = "Créer une nouvelle demande d'enquête", description = "Crée une nouvelle demande d'enquête")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Création réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DemandeEnqueteDTO createDemande(@Valid @RequestBody DemandeEnqueteRequestDTO dto) {
        return demandeEnqueteService.createDemandeEnquete(dto);
    }

    @PostMapping(value = "/document1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer une nouvelle demande d'enquête avec documents", description = "Crée une nouvelle demande d'enquête et enregistre les fichiers joints")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Création réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
//    public DemandeEnqueteDTO createDemandeAvecDocuments(
//            @RequestPart("demande") @Valid DemandeEnqueteRequestDTO dto,
//            @RequestPart(name = "documents", required = false) MultipartFile[] documents
//    ) {
//        return demandeEnqueteService.createDemandeEnqueteAvecDocuments(dto, documents);
//    }
    public DemandeEnqueteDTO createDemandeAvecDocuments1(
            @RequestPart(value = "demande", required = true)
            @Valid @RequestBody DemandeEnqueteRequestDTO dto,
            @RequestPart(name = "documents", required = false) MultipartFile[] documents
    ) {
        return demandeEnqueteService.createDemandeEnqueteAvecDocuments(dto, documents);
    }

//    @PostMapping(value = "/test", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @ResponseStatus(HttpStatus.CREATED)
//    @Operation(summary = "Créer une nouvelle demande d'enquête avec documents",
//            description = "Crée une nouvelle demande d'enquête et enregistre les fichiers joints")
//    @ApiResponses({
//            @ApiResponse(responseCode = "201", description = "Création réussie"),
//            @ApiResponse(responseCode = "400", description = "Requête invalide"),
//            @ApiResponse(responseCode = "500", description = "Erreur serveur")
//    })
//    public DemandeEnqueteDTO test(
//            @RequestPart("files") MultipartFile[] files,
//            @RequestParam("objet") String objet,
//            @RequestParam("description") String description,
//            @RequestParam("urgent") Boolean urgent,
//            @RequestParam("priorite") int priorite,
//            @RequestParam("utilisateurId") Long utilisateurId,
//            @RequestParam(value = "concerneId", required = false) Long concerneId,
////            @RequestPart(value = "concerne", required = false) ConcerneRequestDTO concerne,
//            @RequestParam("dateEcheance")
//            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//            LocalDateTime dateEcheance,
//            @RequestPart(value = "concerne", required = false) String concerneJson
//    ) {
//        DemandeEnqueteRequestDTO demande = DemandeEnqueteRequestDTO.builder()
//                .objet(objet)
//                .urgent(urgent)
//                .priorite(priorite)
//                .description(description)
//                .concerneId(concerneId)
////                .concerne(concerne)
//                .utilisateurId(utilisateurId)
//                .build();
//
//        System.out.println(demande.toString());
//        System.out.println(concerneJson);
//        return null;
//    }
//

//    @PostMapping(value = "/test", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @ResponseStatus(HttpStatus.CREATED)
//    @Operation(
//            summary = "Créer une nouvelle demande d'enquête avec documents",
//            description = "Crée une nouvelle demande d'enquête et enregistre les fichiers joints"
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "201", description = "Création réussie"),
//            @ApiResponse(responseCode = "400", description = "Requête invalide"),
//            @ApiResponse(responseCode = "500", description = "Erreur serveur")
//    })
//    public DemandeEnqueteDTO test(
//            @RequestPart(value = "files", required = false) MultipartFile[] files,
//            @RequestPart("demande") DemandeEnqueteRequestDTO demande
//    ) {
//        System.out.println(demande);
//        if (files != null) {
//            System.out.println("Nb fichiers = " + files.length);
//        }
//        return null;
//    }

//    @PostMapping(value = "/test", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @Operation(
//            summary = "Créer une demande",
//            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    content = @Content(
//                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
//                            schema = @Schema(type = "object"),
//                            encoding = {
//                                    @Encoding(name = "files", contentType = "application/octet-stream"),
//                                    @Encoding(name = "demande", contentType = "application/json")
//                            }
//                    )
//            )
//    )
//    @ResponseStatus(HttpStatus.CREATED)
//    public DemandeEnqueteDTO test(
//            @RequestPart(value = "files", required = false) MultipartFile[] files,
//            @RequestPart("demande") DemandeEnqueteRequestDTO demande
//    ) {
//        System.out.println("Objet = " + demande.getObjet());
//        return null;
//    }

//    @PostMapping(value = "/test", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @Operation(summary = "Créer une demande")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Demande créée")
//    })
//    public DemandeEnqueteDTO test(
//            @Parameter(description = "Fichiers à uploader")
//            @RequestPart(value = "files", required = false) MultipartFile[] files,
//
//            @Parameter(description = "Objet JSON de la demande")
//            @RequestPart("demande") DemandeEnqueteRequestDTO demande
//    ) {
//        System.out.println("Objet = " + demande.getObjet());
//        return null;
//    }

    @PostMapping(value = "/test", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer une demande")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Demande créée")
    })
    public DemandeEnqueteDTO test(
            @Parameter(description = "Fichiers à uploader")
            @RequestPart(value = "files", required = false) MultipartFile[] files,
            @Parameter(description = "Objet JSON de la demande")
            @RequestPart("demande") DemandeEnqueteRequestDTO demande) {
                if (files != null) {
            System.out.println("Nb fichiers = " + files.length);
        }
        System.out.println(demande.toString());
      return  null;
    }




    @PostMapping(value = "/document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer une nouvelle demande d'enquête avec documents", description = "Crée une nouvelle demande d'enquête et enregistre les fichiers joints")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Création réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    public DemandeEnqueteDTO createDemandeAvecDocuments(
            
            @RequestPart(value = "demande", required = true) @Valid DemandeEnqueteRequestDTO dto, // @RequestBody a été supprimé
            @RequestPart(name = "documents", required = false) MultipartFile[] documents
    ) {
        return demandeEnqueteService.createDemandeEnqueteAvecDocuments(dto, documents);
    }




    @Operation(summary = "Modifier une demande d'enquête", description = "Met à jour une demande d'enquête existante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Modification réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DemandeEnqueteDTO updateDemande(
            @PathVariable Long id,
            @Valid @RequestBody DemandeEnqueteUpdateRequestDTO dto) {
        return demandeEnqueteService.updateDemandeEnquete(id, dto);
    }

    @Operation(summary = "Lister les demandes d'enquête", description = "Retourne la liste paginée des demandes d'enquête")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<DemandeEnqueteDTO> listDemandes(
            Pageable pageable,
            @Parameter(description = "Identifiant de l'utilisateur")
            @RequestParam(required = false) Long utilisateurId,
            @Parameter(description = "L'urgence de la demande")
            @RequestParam(required = false) Boolean urgent,
            @Parameter(description = "L'objet de la demande")
            @RequestParam(required = false) String objet,
            @Parameter(description = "Le type de concerne de la demanade: (employeur, beneficiaire, travailleur)")
            @RequestParam(required = false) String type,
            @Parameter(description = "Le code de l'état")
            @RequestParam(required = false) String etat,
            @Parameter(description = "Le code de l'état l'enquête si la demande est validé")
            @RequestParam(required = false) String etatEnquete,
            @Parameter(description = "La priorité de la demande")
            @RequestParam(required = false) Integer priorite,
            @Parameter(description = "Terme de recherche global (nom, fiabilité, etc.)")
            @RequestParam(required = false) String search
            ) {
        return demandeEnqueteService.findAllDemandeEnquete(utilisateurId, urgent, objet, type, etat,etatEnquete, priorite, search, pageable);
    }

    @Operation(summary = "Récupérer une demande d'enquête par ID", description = "Retourne une demande d'enquête via son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Succès"),
            @ApiResponse(responseCode = "404", description = "Demande non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DemandeEnqueteDTO getDemandeById(@PathVariable Long id) {
        return demandeEnqueteService.findDemandeEnqueteById(id);
    }

    @Operation(summary = "Supprimer une demande d'enquête", description = "Supprime une demande par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Suppression réussie"),
            @ApiResponse(responseCode = "404", description = "Demande non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDemande(@PathVariable Long id) {
        demandeEnqueteService.deleteDemandeEnquete(id);
    }

    @Operation(summary = "Changer l'état d'une demande", description = "Change l'état d'une demande d'enquête via son code d'état")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Changement effectué"),
            @ApiResponse(responseCode = "400", description = "Code état invalide"),
            @ApiResponse(responseCode = "404", description = "Demande ou état non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PatchMapping("/{id}/etat")
    @ResponseStatus(HttpStatus.OK)
    public DemandeEnqueteDTO changerEtatDemande(
            @PathVariable Long id,
            @RequestParam("code") String nouvelEtatCode) {
        return demandeEnqueteService.changerEtatDemandeEnquete(id, nouvelEtatCode);
    }

}
