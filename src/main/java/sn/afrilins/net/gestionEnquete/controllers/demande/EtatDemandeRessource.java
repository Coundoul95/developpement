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
import sn.afrilins.net.gestionEnquete.services.dto.demande.EtatDemandeDTO;
import sn.afrilins.net.gestionEnquete.services.dto.demande.request.EtatDemandeRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.demande.EtatDemandeService;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/api/etat/demande")
@Tag(name = "/v1/api/etat/demande", description = "etatDemande, controllers")
@RequiredArgsConstructor
@Slf4j
public class EtatDemandeRessource {

    private final EtatDemandeService etatDemandeService;

    @Operation(summary = "Création d'un état de demande", description = "Crée un nouvel état de demande")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Création réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EtatDemandeDTO createEtatDemande(@Valid @RequestBody EtatDemandeRequestDTO etatDemande) {
        return etatDemandeService.createEtatDemande(etatDemande);
    }


    @Operation(summary = "Modification d'un état de demande", description = "Met à jour un état de demande existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modification réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource introuvable"),
            @ApiResponse(responseCode = "401", description = "Non autorisé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EtatDemandeDTO updateEtatDemande(
            @Parameter(description = "Identifiant de l'état de demande", required = true)
            @PathVariable Long id,
            @Valid @RequestBody EtatDemandeDTO etatDemande) {
        etatDemande.setId(id);
        return etatDemandeService.updateEtatDemande(etatDemande);
    }


    @Operation(summary = "Liste des états de demande", description = "Retourne la liste paginée des états de demande")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public Page<EtatDemandeDTO> readEtatDemandes(Pageable pageable) {
        return etatDemandeService.readAllEtatDemandes(pageable);
    }


    @Operation(summary = "Rechercher un état de demande par ID", description = "Retourne un état de demande à partir de son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "État trouvé"),
            @ApiResponse(responseCode = "403", description = "Accès interdit"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public EtatDemandeDTO findEtatDemandeById(@PathVariable Long id) {
        return etatDemandeService.findEtatDemandeById(id);
    }


    @Operation(summary = "Suppression d'un état de demande", description = "Supprime un état de demande par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Supprimé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEtatDemande(
            @Parameter(description = "Identifiant de l'état de demande")
            @PathVariable Long id) {
        etatDemandeService.deleteEtatDemande(id);
    }

}
