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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.SourceInfoDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.request.SourceInfoRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.request.SourceInfoUpdateRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.SourceInfoService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/api/source/info")
@Tag(name = "/v1/api/source/info", description = "SourceInfo controller")
@RequiredArgsConstructor
@Slf4j
public class SourceInfoRessource {

    private final SourceInfoService sourceInfoService;

    @Operation(summary = "Création d'une source d'information", description = "Crée une nouvelle source d'information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Création réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SourceInfoDTO createSourceInfo(@Valid @RequestBody SourceInfoRequestDTO requestDTO) {
        return sourceInfoService.createSourceInfo(requestDTO);
    }


    @Operation(summary = "Modification d'une source d'information", description = "Met à jour une source d'information existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modification réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource introuvable"),
            @ApiResponse(responseCode = "401", description = "Non autorisé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SourceInfoDTO updateSourceInfo(
            @Parameter(description = "Identifiant de la source d'information", required = true)
            @PathVariable Long id,
            @Valid @RequestBody SourceInfoUpdateRequestDTO requestDTO) {
        requestDTO.setId(id);
        return sourceInfoService.updateSourceInfo(requestDTO);
    }

    @Operation(summary = "Rechercher une source d'information par ID", description = "Retourne une source d'information à partir de son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "État trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès interdit"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public SourceInfoDTO findSourceInfoById(@PathVariable Long id) {
        return sourceInfoService.findSourceInfoById(id);
    }

    @Operation(summary = "Suppression d'une source source d'information", description = "Supprime une source d'information par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Supprimé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSourceInfo(
            @Parameter(description = "Identifiant de la source d'information")
            @PathVariable Long id) {
        sourceInfoService.deleteSourceInfo(id);
    }

    @Operation(summary = "Liste des sources d'information", description = "Retourne la liste paginée des sources d'information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public Page<SourceInfoDTO> readSourceInfos(
            Pageable pageable,
            @Parameter(description = "Identifiant de l'utilisateur")
            @RequestParam(required = false) Long utilisateurId,
            @Parameter(description = "Le code de l'état de la source")
            @RequestParam(required = false) String etat,
            @Parameter(description = "Le code du type de la source")
            @RequestParam(required = false) String type,
            @Parameter(description = "Nom de la source")
            @RequestParam(required = false) String nom,
            @Parameter(description = "Niveau de fiabilité de la source")
            @RequestParam(required = false) String niveauFiabilite,
            @Parameter(description = "Terme de recherche global (nom, fiabilité, etc.)")
            @RequestParam(required = false) String search
    ) {
        return sourceInfoService.readAllSourceInfo(utilisateurId, nom, niveauFiabilite, etat, type, search, pageable);
    }


//    @PostMapping(value = "/docs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @ResponseStatus(HttpStatus.CREATED)
//    @Operation(summary = "Créer une source d'information avec upload de documents")
//    @ApiResponses({
//            @ApiResponse(responseCode = "201", description = "Création avec documents réussie"),
//            @ApiResponse(responseCode = "400", description = "Requête invalide"),
//            @ApiResponse(responseCode = "500", description = "Erreur serveur")
//    })
//    public SourceInfoDTO createSourceInfoWithDocuments(
//            @RequestPart("data") @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Données de la source") SourceInfoRequestDTO data,
//            @RequestPart(value = "files", required = false) List<MultipartFile> files
//    ) {
//        return sourceInfoService.createSourceInfoWithFiles(data, files);
//    }

}
