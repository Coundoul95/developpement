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
import sn.afrilins.net.gestionEnquete.services.dto.enquete.EtatEnqueteDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.request.EtatEnqueteRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.EtatEnqueteService;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/api/etat/enquete")
@Tag(name = "/v1/api/etat/enquete", description = "EtatEnquete, controller")
@RequiredArgsConstructor
@Slf4j
public class EtatEnqueteRessource {

    private final EtatEnqueteService etatEnqueteService;

    @Operation(summary = "Création d'un état d'enquête", description = "Crée un nouvel état d'enquête")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Création réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EtatEnqueteDTO createEtatEnquete(@Valid @RequestBody EtatEnqueteRequestDTO etatEnquete) {
        return etatEnqueteService.createEtatEnquete(etatEnquete);
    }

    @Operation(summary = "Modification d'un état d'enquête", description = "Met à jour un état d'enquête existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modification réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource introuvable"),
            @ApiResponse(responseCode = "401", description = "Non autorisé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EtatEnqueteDTO updateEtatEnquete(
            @Parameter(description = "Identifiant de l'état d'enquête", required = true)
            @PathVariable Long id,
            @Valid @RequestBody EtatEnqueteDTO etatEnquete) {
        etatEnquete.setId(id);
        return etatEnqueteService.updateEtatEnquete(etatEnquete);
    }

    @Operation(summary = "Liste des états d'enquête", description = "Retourne la liste paginée des états d'enquête")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public Page<EtatEnqueteDTO> readEtatEnquetes(Pageable pageable) {
        return etatEnqueteService.readAllEtatEnquetes(pageable);
    }

    @Operation(summary = "Rechercher un état d'enquête par ID", description = "Retourne un état d'enquête à partir de son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "État trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès interdit"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public EtatEnqueteDTO findEtatEnqueteById(@PathVariable Long id) {
        return etatEnqueteService.findEtatEnqueteById(id);
    }

    @Operation(summary = "Suppression d'un état d'enquête", description = "Supprime un état d'enquête par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Supprimé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEtatEnquete(
            @Parameter(description = "Identifiant de l'état d'enquête")
            @PathVariable Long id) {
        etatEnqueteService.deleteEtatEnquete(id);
    }
}
