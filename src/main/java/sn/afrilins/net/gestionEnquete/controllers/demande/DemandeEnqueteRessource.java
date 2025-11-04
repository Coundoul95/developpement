package sn.afrilins.net.gestionEnquete.controllers.demande;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import sn.afrilins.net.gestionEnquete.services.dto.demande.demande_enquete.response.*;
import sn.afrilins.net.gestionEnquete.services.dto.demande.demande_enquete.request.DemandeEnqueteRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.demande.demande_enquete.request.DemandeEnqueteUpdateRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.demande.DemandeEnqueteService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/v1/api/demande/enquete")
@Tag(name = "/v1/api/demande/enquete", description = "DemandeEnquete, controllers")
@RequiredArgsConstructor
@Slf4j
public class DemandeEnqueteRessource {

    private final DemandeEnqueteService demandeEnqueteService;

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
    public DemandeEnqueteStatsDTO getStatsEtat(
            @Parameter(description = "ID de l'utilisateur (optionnel)")
            @RequestParam(name = "utilisateurId", required = false) Long utilisateurId
    ) {
        return demandeEnqueteService.getStatsEtat(utilisateurId);
    }

    @Operation(
            summary = "Les statistiques des enquêtes",
            description = "Récupère les statistiques des demandes validées avec enquête"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Statistiques récupérées avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/stats/enquete")
    @ResponseStatus(HttpStatus.OK) // ✅ on met 200 car c'est une récupération (GET), pas une création
    public DemandeValideEnqueteStatsDTO getStatsEtatEnquete(
            @Parameter(description = "ID de l'utilisateur (optionnel)")
            @RequestParam(name = "utilisateurId", required = false) Long utilisateurId
    ) {
        return demandeEnqueteService.getStatsEtatEnquete(utilisateurId);
    }

    @Operation(
            summary = "Statistiques globales ou par utilisateur",
            description = "Retourne les statistiques des demandes (totales, en cours, terminées, taux de réussite). "
                    + "Si un ID utilisateur est fourni, les statistiques seront filtrées pour cet utilisateur."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Statistiques récupérées avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide (paramètre incorrect)"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public DemandeStatistiquesDTO getStats(
            @Parameter(description = "ID de l'utilisateur (optionnel, si non fourni → statistiques globales)")
            @RequestParam(name = "utilisateurId", required = false) Long utilisateurId
    ) {
        return demandeEnqueteService.getStatistiques(utilisateurId);
    }

    @Operation(
            summary = "Évolution des demandes",
            description = "Retourne l'évolution quotidienne des demandes créées et traitées "
                    + "sur une période donnée (par défaut 7 jours). "
                    + "Si un ID utilisateur est fourni, l'évolution sera filtrée pour cet utilisateur."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Évolution récupérée avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide (paramètre incorrect)"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping("/stats/evolution/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<DemandeSerieDTO> getStatsEvolution(
            @PathVariable Long id,
            @Parameter(description = "Une date quelconque à analyser, par exemple : 2025-07-08.")
            @RequestParam(value = "date", required = false) String date
    ) {
        LocalDate referenceDate = null;
        if (date != null) {
            referenceDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        }

        System.out.println(referenceDate);
        return demandeEnqueteService.getEvolutionDemandes(id, referenceDate);
    }


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
            @Valid @RequestBody DemandeEnqueteUpdateRequestDTO dto

    ) {
        return demandeEnqueteService.updateDemandeEnquete(id, dto);
    }

    @Operation(summary = "Modifier une demande d'enquête avec documents", description = "Met à jour une demande et ajoute des fichiers joints")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Modification réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PutMapping(value = "/{id}/document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public DemandeEnqueteDTO updateDemandeAvecDocument(
            @PathVariable Long id,
            @RequestPart(value = "demande", required = true) @Valid DemandeEnqueteUpdateRequestDTO dto,
            @RequestPart(name = "documents", required = false) MultipartFile[] documents
    ) {
        return demandeEnqueteService.updateDemandeEnqueteAvecDocuments(id, dto, documents);
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
