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
import sn.afrilins.net.gestionEnquete.services.dto.enquete.autre_info.request.AutreInfoUpdateRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.autre_info.response.AutreInfoDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.autre_info.request.AutreInfoRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.AutreInfoService;

import javax.validation.Valid;

//@Hidden
@RestController
@RequestMapping("/v1/api/autre/info")
@Tag(name = "/v1/api/autre/info", description = "AutreInfo, controller")
@RequiredArgsConstructor
@Slf4j
public class AutreInfoRessource {

    private final AutreInfoService autreInfoService;

    @Operation(summary = "Création d'une autre info", description = "Crée une nouvelle autre info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Création réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AutreInfoDTO createAutreInfo(@Valid @RequestBody AutreInfoRequestDTO autreInfo) {
        return autreInfoService.createAutreInfo(autreInfo);
    }

    @Operation(summary = "Modification d'une autre info", description = "Met à jour une autre info existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modification réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource introuvable"),
            @ApiResponse(responseCode = "401", description = "Non autorisé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AutreInfoDTO updateAutreInfo(
            @Parameter(description = "Identifiant de l'autre info", required = true)
            @PathVariable Long id,
            @Valid @RequestBody AutreInfoUpdateRequestDTO request) {
        return autreInfoService.updateAutreInfo(id, request);
    }

    @Operation(summary = "Liste des autres infos", description = "Retourne la liste paginée des autres infos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public Page<AutreInfoDTO> readAutreInfos(
            Pageable pageable,
            @Parameter(description = "La catégorie") @RequestParam(required = false) String categorie,
            @Parameter(description = "Recherche global") @RequestParam(required = false) String search,
            @Parameter(description = "L'objet") @RequestParam(required = false) String objet,
            @Parameter(description = "La description") @RequestParam(required = false) String description,
            @Parameter(description = "le code de l'état") @RequestParam(required = false) String codeEtat,
            @Parameter(description = "L'importance") @RequestParam(required = false) Integer importance) {
        return autreInfoService.readAllAutreInfos(categorie, objet, description, importance, codeEtat, search, pageable);
    }

    @Operation(summary = "Rechercher une autre info par ID", description = "Retourne une autre info à partir de son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Info trouvée"),
            @ApiResponse(responseCode = "403", description = "Accès interdit"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public AutreInfoDTO findAutreInfoById(@PathVariable Long id) {
        return autreInfoService.findAutreInfoById(id);
    }

    @Operation(summary = "Suppression d'une autre info", description = "Supprime une autre info par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Supprimé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAutreInfo(
            @Parameter(description = "Identifiant de l'autre info")
            @PathVariable Long id) {
        autreInfoService.deleteAutreInfo(id);
    }
}
