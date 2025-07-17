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
import sn.afrilins.net.gestionEnquete.services.dto.enquete.TypeEvenementDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.request.TypeEvenementRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.TypeEvenementService;

import javax.validation.Valid;


@RestController
@RequestMapping("/v1/api/type/evenement")
@Tag(name = "/v1/api/type/evenement", description = "TypeEvenement controller")
@RequiredArgsConstructor
@Slf4j
public class TypeEvenementRessource {

    private final TypeEvenementService typeEvenementService;

    @Operation(summary = "Création d'un type d'événement", description = "Crée un nouveau type d'événement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Création réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TypeEvenementDTO createTypeEvenement(@Valid @RequestBody TypeEvenementRequestDTO dto) {
        return typeEvenementService.createTypeEvenement(dto);
    }

    @Operation(summary = "Modification d'un type d'événement", description = "Met à jour un type d'événement existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modification réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource introuvable"),
            @ApiResponse(responseCode = "401", description = "Non autorisé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TypeEvenementDTO updateTypeEvenement(
            @Parameter(description = "Identifiant du type d'événement", required = true)
            @PathVariable Long id,
            @Valid @RequestBody TypeEvenementDTO dto) {
        dto.setId(id);
        return typeEvenementService.updateTypeEvenement(dto);
    }

    @Operation(summary = "Liste des types d'événements", description = "Retourne la liste paginée des types d'événements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<TypeEvenementDTO> readTypeEvenements(
            Pageable pageable,
            @Parameter(description = "Le code") @RequestParam(required = false) String code,
            @Parameter(description = "Le libellé") @RequestParam(required = false) String libelle
    ) {
        return typeEvenementService.readAllTypeEvenements(code, libelle, pageable);
    }

    @Operation(summary = "Rechercher un type d'événement par ID", description = "Retourne un type d'événement à partir de son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Type trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès interdit"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TypeEvenementDTO findTypeEvenementById(@PathVariable Long id) {
        return typeEvenementService.findTypeEvenementById(id);
    }

    @Operation(summary = "Suppression d'un type d'événement", description = "Supprime un type d'événement par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Supprimé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTypeEvenement(
            @Parameter(description = "Identifiant du type d'événement")
            @PathVariable Long id) {
        typeEvenementService.deleteTypeEvenement(id);
    }
}
