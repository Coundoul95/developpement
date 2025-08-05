package sn.afrilins.net.gestionEnquete.controllers.demande;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sn.afrilins.net.gestionEnquete.services.dto.demande.DemandeEnqueteDTO;
import sn.afrilins.net.gestionEnquete.services.dto.demande.request.DemandeEnqueteRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.demande.request.DemandeEnqueteUpdateRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.demande.DemandeEnqueteService;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/api/demande/enquete")
@Tag(name = "/v1/api/demande/enquete", description = "DemandeEnquete, controllers")
@RequiredArgsConstructor
@Slf4j
public class DemandeEnqueteRessource {

    private final DemandeEnqueteService demandeEnqueteService;

    @Operation(summary = "Créer une nouvelle demande d'enquête", description = "Crée une nouvelle demande d'enquête")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Création réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DemandeEnqueteDTO createDemande(@Valid @RequestBody DemandeEnqueteRequestDTO dto) {
        return demandeEnqueteService.createDemandeEnquete(dto);
    }

    @Operation(summary = "Modifier une demande d'enquête", description = "Met à jour une demande d'enquête existante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Modification réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Ressource non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DemandeEnqueteDTO updateDemande(
            @PathVariable Long id,
            @Valid @RequestBody DemandeEnqueteUpdateRequestDTO dto) {
        return demandeEnqueteService.updateDemandeEnquete(id, dto);
    }

    @Operation(summary = "Lister les demandes d'enquête", description = "Retourne la liste paginée des demandes d'enquête")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<DemandeEnqueteDTO> listDemandes(Pageable pageable) {
        return demandeEnqueteService.findAllDemandeEnquete(pageable);
    }

    @Operation(summary = "Récupérer une demande d'enquête par ID", description = "Retourne une demande d'enquête via son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Succès"),
            @ApiResponse(responseCode = "404", description = "Demande non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DemandeEnqueteDTO getDemandeById(@PathVariable Long id) {
        return demandeEnqueteService.findDemandeEnqueteById(id);
    }

    @Operation(summary = "Supprimer une demande d'enquête", description = "Supprime une demande par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Suppression réussie"),
            @ApiResponse(responseCode = "404", description = "Demande non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDemande(@PathVariable Long id) {
        demandeEnqueteService.deleteDemandeEnquete(id);
    }

    @Operation(summary = "Changer l'état d'une demande", description = "Change l'état d'une demande d'enquête via son code d'état")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Changement effectué"),
            @ApiResponse(responseCode = "400", description = "Code état invalide"),
            @ApiResponse(responseCode = "404", description = "Demande ou état non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    @PatchMapping("/{id}/etat")
    @ResponseStatus(HttpStatus.OK)
    public DemandeEnqueteDTO changerEtatDemande(
            @PathVariable Long id,
            @RequestParam("code") String nouvelEtatCode) {
        return demandeEnqueteService.changerEtatDemandeEnquete(id, nouvelEtatCode);
    }

}
