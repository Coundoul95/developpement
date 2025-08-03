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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.EvenementCalendrierDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.StatistiqueCalendrierDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.request.EvenementCalendrierRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.EvenementCalendrierService;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/api/evenement/calendrier")
@Tag(name = "/v1/api/evenement/calendrier", description = "EvenementCalendrier controller")
@RequiredArgsConstructor
@Slf4j
public class EvenementCalendrierRessource {

    private final EvenementCalendrierService evenementCalendrierService;

    @Operation(summary = "Créer un événement", description = "Créer un nouvel événement dans le calendrier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Événement créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EvenementCalendrierDTO> createEvenement(
            @RequestBody EvenementCalendrierRequestDTO dto
    ) {
        return ResponseEntity.ok(evenementCalendrierService.createEvenement(dto));
    }

    @Operation(summary = "Mettre à jour un événement", description = "Mettre à jour les informations d’un événement existant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mise à jour réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Événement non trouvé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EvenementCalendrierDTO> updateEvenement(
            @Parameter(description = "Identifiant de l’événement", required = true)
            @PathVariable Long id,
            @RequestBody EvenementCalendrierRequestDTO dto
    ) {
        EvenementCalendrierDTO updated = evenementCalendrierService.updateEvenement(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Supprimer un événement", description = "Supprimer un événement en utilisant son identifiant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Suppression réussie"),
            @ApiResponse(responseCode = "404", description = "Événement non trouvé")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvenement(
            @Parameter(description = "Identifiant de l’événement à supprimer", required = true)
            @PathVariable Long id
    ) {
        evenementCalendrierService.deleteEvenement(id);
    }

    @Operation(summary = "Récupérer un événement", description = "Récupérer un événement calendrier par son identifiant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Événement trouvé"),
            @ApiResponse(responseCode = "404", description = "Événement non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EvenementCalendrierDTO> findById(
            @Parameter(description = "Identifiant de l’événement", required = true)
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(evenementCalendrierService.findEvenementById(id));
    }

    @Operation(summary = "Lister les événements", description = "Retourne une liste paginée des événements avec filtres dynamiques.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    })
    @GetMapping("/all")
    public ResponseEntity<Page<EvenementCalendrierDTO>> findAllEvenementCalendrier(
            @Parameter(description = "Recherche globale") @RequestParam(required = false) String search,
            @Parameter(description = "Filtrer par titre") @RequestParam(required = false) String titre,
            @Parameter(description = "Filtrer par heure exacte") @RequestParam(required = false) String heure,
            @Parameter(description = "Filtrer par durée") @RequestParam(required = false) Integer duree,
            @Parameter(description = "Filtrer par priorité") @RequestParam(required = false) String priorite,
            @Parameter(description = "Filtrer par date exacte") @RequestParam(required = false) LocalDate date,
            @Parameter(description = "Filtrer par ID utilisateur") @RequestParam(required = false) Long utilisateurId,
            @Parameter(description = "Filtrer par type d’événement") @RequestParam(required = false) String typeCode,
            Pageable pageable
    ) {
        Page<EvenementCalendrierDTO> page = evenementCalendrierService.readAllEvenements(
                search, titre, heure, duree, priorite, date, utilisateurId, typeCode, pageable
        );
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Statistiques hebdomadaires", description = "Retourne les statistiques du calendrier pour l'utilisateur dans la semaine en cours.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistiques calculées avec succès")
    })
    @GetMapping("/statistique/semaine")
    public ResponseEntity<StatistiqueCalendrierDTO> getStatistiquesSemaine(
            @Parameter(description = "Identifiant de l’utilisateur", required = true)
            @RequestParam Long utilisateurId
    ) {
        return ResponseEntity.ok(evenementCalendrierService.getStatistiquesSemaine(utilisateurId));
    }
}
