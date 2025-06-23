package sn.afrilins.net.gestionEnquete.controllers.parametre;

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
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.NotificationDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.NotificationStatsDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.NotificationUpdateDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.request.NotificationRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.NotificationService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/api/notification")
@Tag(name = "/v1/api/notification", description = "notification, controllers")
@RequiredArgsConstructor
@Slf4j
public class NotificationRessource {

    private final NotificationService notificationService;


    @Operation(summary = "Creation notification", description = "Il prend en entre une notification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(responseCode = "400", description = "La requête est syntaxiquement incorrecte"),
            @ApiResponse(responseCode = "500", description = "Problèment du serveur")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationDTO createNotification(@Valid @RequestBody NotificationRequestDTO notification) {
        return notificationService.createNotification(notification);
    }

    @Operation(summary = "Modification notification", description = "il prend en entre une notification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Réussie"),
            @ApiResponse(responseCode = "400", description = "La requête est syntaxiquement incorrect"),
            @ApiResponse(responseCode = "404", description = "La ressource est innacessible"),
            @ApiResponse(responseCode = "401", description = "Ereur d'accès , Veuillez vérifiez votre authentification"),
            @ApiResponse(responseCode = "500", description = "Probléme du serveur")})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NotificationDTO updateNotification(
            @Parameter(description = "Identifiant notification", name = "id", required = true)
            @PathVariable(value = "id") Long id,
            @RequestBody @Valid NotificationUpdateDTO notification) {
        return notificationService.updateNotification(id, notification);
    }

    @Operation(summary = "Suppression d'une notification", description = "Suppression d'une Notification, Il prend en entre un identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Aucun contenu"),
            @ApiResponse(responseCode = "400", description = "La requête est syntaxiquement incorrect"),
            @ApiResponse(responseCode = "404", description = "La ressource n'existe pas"),
            @ApiResponse(responseCode = "500", description = "Problème du serveur")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNotification(
            @Parameter(description = "Identifiant notification", name = "id", required = true)
            @PathVariable(value = "id") Long id) {
        notificationService.deleteNotification(id);
    }

    @Operation(
            summary = "Suppression de plusieurs notifications",
            description = "Supprime une liste de notifications en fournissant leurs identifiants"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notifications supprimées avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Une ou plusieurs notifications introuvables"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur interne")
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteManyNotifications(
            @Parameter(description = "Liste des identifiants des notifications à supprimer", required = true)
            @RequestBody List<Long> ids
    ) {
        notificationService.deleteManyNotification(ids);
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "l'expression de besoin est bien trouve"),
            @ApiResponse(responseCode = "500", description = "le serveur ne fonctionne pas correctement"),
            @ApiResponse(responseCode = "403", description = "probleme accees")
    })
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public NotificationDTO findNotificationById(
            @Parameter(description = "Identifiant notification", name = "id", required = true)
            @PathVariable(value = "id") Long id) {
        return notificationService.findNotificationById(id);
    }


    @Operation(summary = "Liste des notifications", description = "Permet de lister les notifications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public Page<NotificationDTO> readAllNotification(
            @Parameter(description = "l'identifiant d'un utilisateur") @RequestParam(value = "utilisateurId", required = false) Long utilisateurId,
            @Parameter(description = "le type de notification") @RequestParam(value = "typeNotification", required = false) String typeNotification,
            Pageable pageable) {
        return notificationService.readAllNotification(utilisateurId, typeNotification, pageable);
    }

    @Operation(summary = "Marquer une notification comme lu", description = "Permet de maruqer une notification comme lu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @PostMapping("mark/read/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NotificationDTO markAsReadNotification(
            @Parameter(description = "Identifiant notification", name = "id", required = true, example = "1")
            @PathVariable(value = "id") Long id) {
        return notificationService.markAsReadNotification(id);
    }

    @Operation(summary = "Marquer plusieurs notifications comme lues", description = "Permet de marquer plusieurs notifications comme lues")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Succès"),
            @ApiResponse(responseCode = "500", description = "Erreur interne lors du traitement de la requête")
    })
    @PostMapping("/mark/read/many")
    @ResponseStatus(HttpStatus.OK)
    public List<NotificationDTO> markManyAsReadNotification(
            @Parameter(description = "Liste des identifiants de notifications", required = true, example = "[1, 2, 3]")
            @RequestBody List<Long> ids) {
        return notificationService.markManyAsReadNotification(ids);
    }


    @Operation(summary = "Liste des notifications non lus", description = "Permet de lister les notifications non lus")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("all/not/read")
    @ResponseStatus(HttpStatus.OK)
    public Page<NotificationDTO> findAllNonLuesByUtilisateurId(
            @Parameter(description = "l'identifiant de l'utilisateur" , example = "1") @RequestParam(value = "utilisateurId", required = false) Long utilisateurId,
            @Parameter(description = "le type de notification") @RequestParam(value = "typeNotification", required = false) String typeNotification,
            Pageable pageable) {
        return  notificationService.findAllNonLuesByUtilisateurId(utilisateurId, typeNotification, pageable);
    }

    @Operation(summary = "Liste des notifications lus", description = "Permet de lister les notifications lus")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("all/read")
    @ResponseStatus(HttpStatus.OK)
    public Page<NotificationDTO> findAllLuesByUtilisateurId(
             @Parameter(description = "l'identifiant de l'utilisateur") @RequestParam(value = "utilisateurId", required = false) Long utilisateurId,
             @Parameter(description = "le type de notification") @RequestParam(value = "typeNotification", required = false) String typeNotification,
             Pageable pageable) {
        return  notificationService.findAllLuesByUtilisateurId(utilisateurId,typeNotification, pageable);
    }

    @Operation(summary = "Liste des notifications lus", description = "Permet de lister les notifications lus")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("all/urgent")
    @ResponseStatus(HttpStatus.OK)
    public Page<NotificationDTO> findAllUrgentByUtilisateurId(
            @Parameter(description = "l'identifiant de l'utilisateur") @RequestParam(value = "utilisateurId", required = false) Long utilisateurId,
            @Parameter(description = "le type de notification") @RequestParam(value = "typeNotification", required = false) String typeNotification,
            Pageable pageable) {
        return  notificationService.findAllUrgentByUtilisateurId(utilisateurId,typeNotification, pageable);
    }

    @Operation(summary = "Recupérer les statistiques des notifications d'un utilisateur", description = "Permet de récupérer les statistiques des notifications d'un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Succès"),
            @ApiResponse(responseCode = "500", description = "Erreur interne lors du traitement de la requête")
    })
    @GetMapping("/stats/{id}")
    public NotificationStatsDTO getStats(
            @Parameter(description = "Identifiant notification", name = "id", required = true, example = "1")
            @PathVariable(value = "id") Long id
    ) {
        return notificationService.getNotificationStats(id);
    }


}
