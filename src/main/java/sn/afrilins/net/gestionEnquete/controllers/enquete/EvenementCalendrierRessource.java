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
import sn.afrilins.net.gestionEnquete.services.dto.enquete.EvenementCalendrierDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.request.EvenementCalendrierRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.EvenementCalendrierService;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/v1/api/evenement/calendrier")
@Tag(name = "/v1/api/evenement/calendrier", description = "EvenementCalendrier controller")
@RequiredArgsConstructor
@Slf4j
public class EvenementCalendrierRessource {

    private final EvenementCalendrierService evenementCalendrierService;

    @Operation(summary = "Créer un événement", description = "Crée un nouveau événement calendrier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Création réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EvenementCalendrierDTO createEvenement(@Valid @RequestBody EvenementCalendrierRequestDTO dto) {
        return evenementCalendrierService.createEvenement(dto);
    }

    @Operation(summary = "Mettre à jour un événement", description = "Met à jour un événement existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mise à jour réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Événement non trouvé")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EvenementCalendrierDTO updateEvenement(
            @Parameter(description = "Identifiant de l’événement", required = true)
            @PathVariable Long id,
            @Valid @RequestBody EvenementCalendrierRequestDTO dto
    ) {
        return evenementCalendrierService.updateEvenement(id, dto);
    }

    @Operation(summary = "Supprimer un événement", description = "Supprime un événement à partir de son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Suppression réussie"),
            @ApiResponse(responseCode = "404", description = "Événement non trouvé")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvenement(
            @Parameter(description = "Identifiant de l’événement")
            @PathVariable Long id
    ) {
        evenementCalendrierService.deleteEvenement(id);
    }

    @Operation(summary = "Récupérer un événement par ID", description = "Retourne un événement calendrier à partir de son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Événement trouvé"),
            @ApiResponse(responseCode = "404", description = "Événement non trouvé")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EvenementCalendrierDTO findById(
            @Parameter(description = "Identifiant de l’événement")
            @PathVariable Long id
    ) {
        return evenementCalendrierService.findEvenementById(id);
    }

    @Operation(summary = "Liste des événements", description = "Retourne une liste paginée des événements avec filtres")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Succès")
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<EvenementCalendrierDTO> findAllEvenementCalendrier(
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String heure,
            @RequestParam(required = false) Integer duree,
            @RequestParam(required = false) String priorite,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) Long utilisateurId,
            @RequestParam(required = false) String typeCode
    ) {
        return evenementCalendrierService.readAllEvenements(
                search, titre, heure, duree, priorite, date, utilisateurId, typeCode, pageable
        );
    }
}
