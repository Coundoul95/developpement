package sn.afrilins.net.gestionEnquete.controllers.enquete;

import io.swagger.v3.oas.annotations.Hidden;
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
import sn.afrilins.net.gestionEnquete.services.dto.enquete.conclusion.response.ConclusionDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.conclusion.request.ConclusionRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.ConclusionService;

import javax.validation.Valid;

@Hidden
@RestController
@RequestMapping("/v1/api/conclusion")
@Tag(name = "/v1/api/conclusion", description = "Conclusion, controller")
@RequiredArgsConstructor
@Slf4j
public class ConclusionRessource {

    private final ConclusionService conclusionService;

    @Operation(summary = "Création d'une conclusion", description = "Crée une nouvelle conclusion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Création réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConclusionDTO createConclusion(@Valid @RequestBody ConclusionRequestDTO conclusion) {
        return conclusionService.createConclusion(conclusion);
    }

    @Operation(summary = "Modification d'une conclusion", description = "Met à jour une conclusion existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modification réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource introuvable"),
            @ApiResponse(responseCode = "401", description = "Non autorisé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ConclusionDTO updateConclusion(
            @Parameter(description = "Identifiant de la conclusion", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ConclusionDTO conclusion) {
        conclusion.setId(id);
        return conclusionService.updateConclusion(conclusion);
    }

    @Operation(summary = "Liste des conclusions", description = "Retourne la liste paginée des conclusions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public Page<ConclusionDTO> readConclusions(
            Pageable pageable,
            @Parameter(description = "Le titre") @RequestParam(required = false) String titre,
            @Parameter(description = "Le contenu") @RequestParam(required = false) String contenu,
            @Parameter(description = "La recommandation") @RequestParam(required = false) String recommandation,
            @Parameter(description = "Les mesures de suivi") @RequestParam(required = false) String mesuresSuivi) {
        return conclusionService.readAllConclusions(titre, contenu, recommandation, mesuresSuivi, pageable);
    }

    @Operation(summary = "Rechercher une conclusion par ID", description = "Retourne une conclusion à partir de son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conclusion trouvée"),
            @ApiResponse(responseCode = "403", description = "Accès interdit"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ConclusionDTO findConclusionById(@PathVariable Long id) {
        return conclusionService.findConclusionById(id);
    }

    @Operation(summary = "Suppression d'une conclusion", description = "Supprime une conclusion par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Supprimé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteConclusion(
            @Parameter(description = "Identifiant de la conclusion")
            @PathVariable Long id) {
        conclusionService.deleteConclusion(id);
    }
}
