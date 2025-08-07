package sn.afrilins.net.gestionEnquete.controllers.enquete;

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
import org.springframework.web.bind.annotation.*;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.EnqueteDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.EnqueteService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/v1/api/enquete")
@Tag(name = "/v1/api/enquete", description = "Enquete, controller")
@RequiredArgsConstructor
@Slf4j
public class EnqueteRessource {
    private final EnqueteService enqueteService;

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

    @Operation(summary = "Lister toutes les enquêtes", description = "Retourne la liste paginée des enquêtes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<EnqueteDTO> listEnquetes(
            Pageable pageable,
            @Parameter(description = "Le code de l'état") @RequestParam(required = false) String etatCode,
            @Parameter(description = "La progression") @RequestParam(required = false) Integer progression,
            @Parameter(description = "Le code de l'état") @RequestParam(required = false) LocalDateTime dateDebut,
            @Parameter(description = "La progression") @RequestParam(required = false) LocalDateTime dateFin
    ) {
        log.info("Récupération de la liste paginée des enquêtes");
        return enqueteService.readAllEnquete(etatCode, progression, dateDebut, dateFin, pageable);
    }

    @Operation(summary = "Récupérer une enquête par ID", description = "Retourne une enquête via son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Succès"),
            @ApiResponse(responseCode = "404", description = "Enquête non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EnqueteDTO getEnqueteById(@PathVariable Long id) {
        log.info("Récupération de l'enquête ID {}", id);
        return enqueteService.findEnqueteById(id);
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
    @PatchMapping("/{demandeId}/etat")
    @ResponseStatus(HttpStatus.OK)
    public EnqueteDTO changerEtatEnquete(
            @PathVariable Long demandeId,
            @RequestParam("code") String nouvelEtatCode) {
        log.info("Changement d'état de l'enquête liée à la demande ID {} en état {}", demandeId, nouvelEtatCode);
        return enqueteService.changerEtatEnquete(demandeId, nouvelEtatCode);
    }

}
