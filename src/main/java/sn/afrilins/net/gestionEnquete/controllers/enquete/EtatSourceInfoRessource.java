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
import sn.afrilins.net.gestionEnquete.services.dto.enquete.source_info.response.EtatSourceInfoDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.source_info.request.EtatSourceInfoRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.EtatSourcInfoService;

import javax.validation.Valid;

@Hidden
@RestController
@RequestMapping("/v1/api/etat/source")
@Tag(name = "/v1/api/etat/source", description = "EtatSourceInfo, controller")
@RequiredArgsConstructor
@Slf4j
public class EtatSourceInfoRessource {

    private final EtatSourcInfoService etatSourcInfoService;
    @Operation(summary = "Créer un état source info", description = "Crée une nouvelle source d'information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Création réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EtatSourceInfoDTO createEtatSourceInfo(@Valid @RequestBody EtatSourceInfoRequestDTO dto) {
        return etatSourcInfoService.createEtatSourceInfo(dto);
    }

    @Operation(summary = "Modifier un état source info", description = "Met à jour une source d'information existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modification réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EtatSourceInfoDTO updateEtatSourceInfo(
            @Parameter(description = "ID de la source", required = true) @PathVariable Long id,
            @Valid @RequestBody EtatSourceInfoDTO dto) {
        dto.setId(id);
        return etatSourcInfoService.updateEtatSourceInfo(dto);
    }

    @Operation(summary = "Lister les sources d'information", description = "Retourne toutes les sources paginées")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<EtatSourceInfoDTO> readEtatSourceInfos(
            Pageable pageable,
            @Parameter(description = "Le code") @RequestParam(required = false) String code,
            @Parameter(description = "Le libelle") @RequestParam(required = false) String libelle) {
        return etatSourcInfoService.readAllEtatSourceInfos(code, libelle, pageable);
    }

    @Operation(summary = "Rechercher une source par ID", description = "Retourne une source d'information selon son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trouvé"),
            @ApiResponse(responseCode = "404", description = "Non trouvé")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EtatSourceInfoDTO findEtatSourceInfoById(@PathVariable Long id) {
        return etatSourcInfoService.findEtatSourceInfoById(id);
    }

    @Operation(summary = "Supprimer une source d'information", description = "Supprime une source d'information par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Supprimée"),
            @ApiResponse(responseCode = "404", description = "Non trouvée")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEtatSourceInfo(@PathVariable Long id) {
        etatSourcInfoService.deleteEtatSourceInfo(id);
    }
}
