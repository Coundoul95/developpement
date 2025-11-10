package sn.afrilins.net.gestionEnquete.controllers.enquete;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.afrilins.net.gestionEnquete.domain.enume.TypeConcerne;
import sn.afrilins.net.gestionEnquete.services.dto.demande.demande_enquete.request.DemandeEnqueteRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.request.EnqueteDocumentRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.request.EnqueteSourceRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.response.EnqueteAllDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.response.EnqueteAvecDemandeDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.request.EnqueteAssignationRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.response.EnqueteDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.response.EnqueteStatsDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.EnqueteService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/api/enquete")
@Tag(name = "/v1/api/enquete", description = "Enquete, controller")
@RequiredArgsConstructor
@Slf4j
public class EnqueteRessource {
    private final EnqueteService enqueteService;


    @Operation(
            summary = "Statistiques des demandes d'enquête",
            description = "Retourne les statistiques globales ou celles d'un utilisateur donné"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Statistiques récupérées avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/stats/etat")
    @ResponseStatus(HttpStatus.OK) // ✅ on met 200 car c'est une récupération (GET), pas une création
    public EnqueteStatsDTO getStatsEtat(
            @Parameter(description = "ID de l'utilisateur (optionnel)")
            @RequestParam(name = "utilisateurId", required = false) Long utilisateurId
    ) {
        return enqueteService.getStatsEtat(utilisateurId);
    }

    @Operation(summary = "Créer une nouvelle enquête liée à une demande", description = "Crée une nouvelle enquête à partir de l'ID d'une demande d'enquête")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Création réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping("/demande/{demandeId}")
    @ResponseStatus(HttpStatus.CREATED)
    public EnqueteDTO createEnquete(@PathVariable Long demandeId) {
        log.info("Création d'une enquête pour la demande d'ID {}", demandeId);
        return enqueteService.createEnquete(demandeId);
    }

    @Operation(summary = "Créer une nouvelle enquête liée à une demande", description = "Crée une nouvelle enquête à partir de l'ID d'une demande d'enquête")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Création réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping("/assigner/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public EnqueteDTO assignerEnquete(
            @PathVariable Long id,
            @RequestBody @Valid EnqueteAssignationRequestDTO dto
    ) {
        log.info("Assignation d'un enquêteur à l'enquête {}", id);
        return enqueteService.assignerEnqueteur(id, dto);
    }

    @Operation(summary = "Mettre à jour une enquête", description = "Met à jour les informations d'une enquête existante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mise à jour réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Enquête non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public EnqueteDTO updateEnquete(@RequestBody EnqueteDTO dto) {
        log.info("Mise à jour de l'enquête ID {}", dto.getId());
        return enqueteService.updateEnquete(dto);
    }

    @Operation(
            summary = "Lister toutes les enquêtes",
            description = "Retourne la liste paginée des enquêtes avec leur demande associée. "
                    + "Possibilité de filtrer par état, progression, dates, assignation et enquêteur."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/all/avec/demande")
    @ResponseStatus(HttpStatus.OK)
    public Page<EnqueteAvecDemandeDTO> listEnquetes(
            Pageable pageable,
            @Parameter(description = "Le code de l'état")
            @RequestParam(required = false) String etatCode,
            @Parameter(description = "La progression")
            @RequestParam(required = false) Integer progression,
            @Parameter(description = "Progression minimale (0 à 100)")
            @RequestParam(required = false) Integer progressionMin,
            @Parameter(description = "Progression maximale (0 à 100)")
            @RequestParam(required = false) Integer progressionMax,
            @Parameter(description = "Date de début (format ISO-8601)")
            @RequestParam(required = false) LocalDateTime dateDebut,
            @Parameter(description = "Date de fin (format ISO-8601)")
            @RequestParam(required = false) LocalDateTime dateFin,
            @Parameter(description = "Filtrer par assignation (true = assignée, false = non assignée)")
            @RequestParam(required = false) Boolean assignee,
            @Parameter(description = "ID de l'enquêteur assigné")
            @RequestParam(required = false) Long enqueteurId,
            @Parameter(description = "Recherche global")
            @RequestParam(required = false) String search,
            @Parameter(description = "Le type de concerne de la demanade: (employeur, beneficiaire, travailleur)")
            @RequestParam(required = false) String type,
            @Parameter(description = "La priorité de la demande")
            @RequestParam(required = false) Integer priorite,
            @Parameter(description = "L'urgence de la demande")
            @RequestParam(required = false) Boolean urgent
    ) {
        return enqueteService.readAllEnqueteAvecDemande(
                etatCode,
                progression,
                progressionMin,
                progressionMax,
                dateDebut,
                dateFin,
                assignee,
                enqueteurId,
                search,
                type,
                priorite,
                urgent,
                pageable
        );
    }


    @Operation(
            summary = "Lister toutes les enquêtes",
            description = "Retourne la liste paginée des enquêtes sans leur demande associée. "
                    + "Possibilité de filtrer par état, progression, dates, assignation et enquêteur."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/all/sans/demande")
    @ResponseStatus(HttpStatus.OK)
    public Page<EnqueteDTO> listEnquetesSansDemande(
            Pageable pageable,
            @Parameter(description = "Le code de l'état")
            @RequestParam(required = false) String etatCode,
            @Parameter(description = "La progression de l'enquête (0 à 100)")
            @RequestParam(required = false) Integer progression,
            @Parameter(description = "Progression minimale (0 à 100)")
            @RequestParam(required = false) Integer progressionMin,
            @Parameter(description = "Progression maximale (0 à 100)")
            @RequestParam(required = false) Integer progressionMax,
            @Parameter(description = "Date de début (format ISO-8601)")
            @RequestParam(required = false) LocalDateTime dateDebut,
            @Parameter(description = "Date de fin (format ISO-8601)")
            @RequestParam(required = false) LocalDateTime dateFin,
            @Parameter(description = "Filtrer par assignation (true = assignée, false = non assignée)")
            @RequestParam(required = false) Boolean assignee,
            @Parameter(description = "ID de l'enquêteur assigné")
            @RequestParam(required = false) Long enqueteurId,
            @Parameter(description = "Recherche global")
            @RequestParam(required = false) String search,
            @Parameter(description = "Le type de concerne de la demanade: (employeur, beneficiaire, travailleur)")
            @RequestParam(required = false) String type,
            @Parameter(description = "La priorité de la demande")
            @RequestParam(required = false) Integer priorite,
            @Parameter(description = "L'urgence de la demande")
            @RequestParam(required = false) Boolean urgent
    ) {
        log.info("Récupération de la liste paginée des enquêtes sans demande avec filtres : "
                        + "etatCode={}, progression={}, dateDebut={}, dateFin={}, assignee={}, enqueteurId={}, search={}",
                etatCode, progression, dateDebut, dateFin, assignee, enqueteurId, search
        );

        return enqueteService.readAllEnqueteSansDemande(
                etatCode,
                progression,
                progressionMin,
                progressionMax,
                dateDebut,
                dateFin,
                assignee,
                enqueteurId,
                search,
                type,
                priorite,
                urgent,
                pageable
        );
    }


    @Operation(summary = "Récupérer une enquête par ID", description = "Retourne une enquête via son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Succès"),
            @ApiResponse(responseCode = "404", description = "Enquête non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EnqueteAvecDemandeDTO getEnqueteById(@PathVariable Long id) {
        log.info("Récupération de l'enquête ID {}", id);
        return enqueteService.findEnqueteById(id);
    }

    @Operation(summary = "Récupérer une enquête par ID avec tous ces données", description = "Retourne une enquête via son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Succès"),
            @ApiResponse(responseCode = "404", description = "Enquête non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/{id}/all")
    @ResponseStatus(HttpStatus.OK)
    public EnqueteAllDTO getEnqueteByIdAll(@PathVariable Long id) {
        log.info("Récupération de l'enquête ID {}", id);
        return enqueteService.findEnqueteByIdAll(id);
    }


    @Operation(summary = "Supprimer une enquête", description = "Supprime une enquête par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Suppression réussie"),
            @ApiResponse(responseCode = "404", description = "Enquête non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEnquete(@PathVariable Long id) {
        log.info("Suppression de l'enquête ID {}", id);
        enqueteService.deleteEnquete(id);
    }


    @Operation(summary = "Changer l'état d'une enquête", description = "Change l'état d'une enquête via son identifiant et un code d'état")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Changement effectué"),
            @ApiResponse(responseCode = "400", description = "Code état invalide"),
            @ApiResponse(responseCode = "404", description = "Enquête ou état non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PatchMapping("/{enqueteId}/etat")
    @ResponseStatus(HttpStatus.OK)
    public EnqueteDTO changerEtatEnquete(
            @PathVariable Long enqueteId,
            @RequestParam("code") String nouvelEtatCode) {
        log.info("Changement d'état de l'enquête liée à la demande ID {} en état {}", enqueteId, nouvelEtatCode);
        return enqueteService.changerEtatEnquete(enqueteId, nouvelEtatCode);
    }

    @Operation(summary = "Mettre à jour la progression d'une enquête", description = "Met à jour uniquement la progression d'une enquête via son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mise à jour réussie"),
            @ApiResponse(responseCode = "400", description = "Valeur de progression invalide"),
            @ApiResponse(responseCode = "404", description = "Enquête non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PatchMapping("/{enqueteId}/progression")
    @ResponseStatus(HttpStatus.OK)
    public EnqueteDTO updateProgression(
            @PathVariable Long enqueteId,
            @RequestParam("progression") int progression) {
        log.info("Mise à jour de la progression de l'enquête ID {} à {}%", enqueteId, progression);
        return enqueteService.updateProgression(enqueteId, progression);
    }



    @Operation(
            summary = "Associer des documents à une enquête en cours",
            description = "Ajoute de nouveaux documents uploadés et/ou associe des documents déjà existants à une enquête"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Documents associés avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Enquête ou document non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PatchMapping(value = "/{enqueteId}/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public EnqueteDTO ajouterDocumentsAEnquete(
            @PathVariable Long enqueteId,
            @RequestPart(name = "fichiers", required = false) MultipartFile[] fichiers,
//            @RequestPart(name = "documentIds", required = false) List<Long> documentIds
            @RequestPart(value = "document", required = true) @Valid EnqueteDocumentRequestDTO dto

    ) {

        return enqueteService.ajouterDocuments(enqueteId, fichiers, dto);
    }

    @Operation(
            summary = "Associer des source d'information à une enquête en cours",
            description = "Associe des source d'information déjà existants à une enquête"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Source d'information associés avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Enquête ou source d'information non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PatchMapping(value = "/{enqueteId}/sources")
    @ResponseStatus(HttpStatus.OK)
    public EnqueteDTO ajouterSourceAEnquete(
            @PathVariable Long enqueteId,
            @RequestBody @Valid EnqueteSourceRequestDTO dto
    ) {

        return enqueteService.ajouterSources(enqueteId , dto);
    }


}
