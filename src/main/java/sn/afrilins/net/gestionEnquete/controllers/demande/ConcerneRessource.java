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
import org.springframework.web.bind.annotation.*;
import sn.afrilins.net.gestionEnquete.domain.enume.TypeConcerne;
import sn.afrilins.net.gestionEnquete.services.dto.demande.ConcerneDTO;
import sn.afrilins.net.gestionEnquete.services.dto.demande.request.ConcerneRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.demande.ConcerneService;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/api/concerne")
@Tag(name = "/v1/api/concerne", description = "concerne, controllers")
@RequiredArgsConstructor
@Slf4j
public class ConcerneRessource {

    private final ConcerneService concerneService;

    @Operation(summary = "Création d'un concerné", description = "Crée un nouveau concerné")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Création réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConcerneDTO createConcerne(@Valid @RequestBody ConcerneRequestDTO concerne) {
        return concerneService.createConcerne(concerne);
    }

    @Operation(summary = "Modification d'un concerné", description = "Met à jour un concerné existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modification réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource introuvable"),
            @ApiResponse(responseCode = "401", description = "Non autorisé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ConcerneDTO updateConcerne(
            @Parameter(description = "Identifiant du concerné", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ConcerneDTO concerne) {
        concerne.setId(id);
        return concerneService.updateConcerne(concerne);
    }

    @Operation(summary = "Liste des concernés", description = "Retourne la liste paginée des concernés")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<ConcerneDTO> readConcernes(
            @RequestParam(required = false) TypeConcerne type,
            @RequestParam(required = false) String numero,
            @RequestParam(required = false) String regionSocial,
            Pageable pageable) {
        return concerneService.readAllConcernes(type, numero, regionSocial, pageable);
    }

    @Operation(summary = "Rechercher un concerné par ID", description = "Retourne un concerné à partir de son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Concerné trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès interdit"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ConcerneDTO findConcerneById(@PathVariable Long id) {
        return concerneService.findConcerneById(id);
    }

    @Operation(summary = "Suppression d'un concerné", description = "Supprime un concerné par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Supprimé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteConcerne(
            @Parameter(description = "Identifiant du concerné")
            @PathVariable Long id) {
        concerneService.deleteConcerne(id);
    }
}
