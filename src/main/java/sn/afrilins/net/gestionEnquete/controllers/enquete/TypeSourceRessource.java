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
import sn.afrilins.net.gestionEnquete.services.dto.enquete.TypeSourceDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.request.TypeSourceRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.TypeSourceService;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/api/type/source")
@Tag(name = "/v1/api/type/source", description = "TypeSource controller")
@RequiredArgsConstructor
@Slf4j
public class TypeSourceRessource {

    private final TypeSourceService typeSourceService;

    @Operation(summary = "Création d'un type de source", description = "Crée un nouveau type de source")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Création réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TypeSourceDTO createTypeSource(@Valid @RequestBody TypeSourceRequestDTO typeSource) {
        return typeSourceService.createTypeSource(typeSource);
    }

    @Operation(summary = "Modification d'un type de source", description = "Met à jour un type de source existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modification réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource introuvable"),
            @ApiResponse(responseCode = "401", description = "Non autorisé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TypeSourceDTO updateTypeSource(
            @Parameter(description = "Identifiant du type de source", required = true)
            @PathVariable Long id,
            @Valid @RequestBody TypeSourceDTO typeSource) {
        typeSource.setId(id);
        return typeSourceService.updateTypeSource(typeSource);
    }

    @Operation(summary = "Liste des types de source", description = "Retourne la liste paginée des types de source")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public Page<TypeSourceDTO> readTypeSources(
            Pageable pageable,
            @Parameter(description = "Le code") @RequestParam(required = false) String code,
            @Parameter(description = "Le libelle") @RequestParam(required = false) String libelle
            ) {
        return typeSourceService.readAllTypeSources(code, libelle, pageable);
    }

    @Operation(summary = "Rechercher un type de source par ID", description = "Retourne un type de source à partir de son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Type trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès interdit"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public TypeSourceDTO findTypeSourceById(@PathVariable Long id) {
        return typeSourceService.findTypeSourceById(id);
    }

    @Operation(summary = "Suppression d'un type de source", description = "Supprime un type de source par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Supprimé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTypeSource(
            @Parameter(description = "Identifiant du type de source")
            @PathVariable Long id) {
        typeSourceService.deleteTypeSource(id);
    }
}
