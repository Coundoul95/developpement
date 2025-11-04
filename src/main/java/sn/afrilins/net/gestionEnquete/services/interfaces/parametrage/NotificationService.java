package sn.afrilins.net.gestionEnquete.services.interfaces.parametrage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.notification.response.NotificationDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.notification.response.NotificationStatsDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.notification.request.NotificationUpdateDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.document.request.NotificationRequestDTO;

import java.util.List;
/**
 * Interface définissant les opérations de gestion des notifications dans le système.
 */
public interface NotificationService {

    /**
     * Crée une nouvelle notification.
     *
     * @param notification les données de la notification à créer
     * @return la notification créée
     */
    NotificationDTO createNotification(NotificationRequestDTO notification);

    /**
     * Met à jour une notification existante.
     *
     * @param id l'identifiant de la notification à mettre à jour
     * @param notification les nouvelles données de la notification
     * @return la notification mise à jour
     */
    NotificationDTO updateNotification(Long id, NotificationUpdateDTO notification);

    /**
     * Supprime une notification à partir de son identifiant.
     *
     * @param id l'identifiant de la notification à supprimer
     */
    void deleteNotification(Long id);

    /**
     * Supprime plusieurs notifications à partir de leurs identifiants.
     *
     * @param ids les identifiants des notifications à supprimer
     */
    void deleteManyNotification(List<Long> ids);

    /**
     * Récupère une notification selon son identifiant.
     *
     * @param id l'identifiant de la notification
     * @return la notification correspondante, ou {@code null} si elle n'existe pas
     */
    NotificationDTO findNotificationById(Long id);

    /**
     * Récupère toutes les notifications d’un utilisateur de manière paginée,
     * filtrées par type.
     *
     * @param utilisateurId l'identifiant de l'utilisateur concerné
     * @param typeNotification le type de notification à filtrer
     * @param pageable les informations de pagination
     * @return une page de notifications
     */
    Page<NotificationDTO> readAllNotification(Long utilisateurId, String typeNotification, Pageable pageable);

    /**
     * Marque une notification comme lue.
     *
     * @param id l'identifiant de la notification à marquer comme lue
     * @return la notification mise à jour
     */
    NotificationDTO markAsReadNotification(Long id);

    /**
     * Marque plusieurs notifications comme lues.
     *
     * @param ids les identifiants des notifications à marquer comme lues
     * @return la liste des notifications mises à jour
     */
    List<NotificationDTO> markManyAsReadNotification(List<Long> ids);

    /**
     * Récupère toutes les notifications non lues pour un utilisateur, filtrées par type.
     *
     * @param utilisateurId l'identifiant de l'utilisateur
     * @param typeNotification le type de notification à filtrer
     * @param pageable les informations de pagination
     * @return une page de notifications non lues
     */
    Page<NotificationDTO> findAllNonLuesByUtilisateurId(Long utilisateurId, String typeNotification, Pageable pageable);

    /**
     * Récupère toutes les notifications lues pour un utilisateur, filtrées par type.
     *
     * @param utilisateurId l'identifiant de l'utilisateur
     * @param typeNotification le type de notification à filtrer
     * @param pageable les informations de pagination
     * @return une page de notifications lues
     */
    Page<NotificationDTO> findAllLuesByUtilisateurId(Long utilisateurId, String typeNotification, Pageable pageable);

    /**
     * Récupère toutes les notifications urgentes pour un utilisateur, filtrées par type.
     *
     * @param utilisateurId l'identifiant de l'utilisateur
     * @param typeNotification le type de notification à filtrer
     * @param pageable les informations de pagination
     * @return une page de notifications urgentes
     */
    Page<NotificationDTO> findAllUrgentByUtilisateurId(Long utilisateurId, String typeNotification, Pageable pageable);

    /**
     * Récupère les statistiques globales des notifications d’un utilisateur.
     *
     * @param utilisateurId l'identifiant de l'utilisateur concerné
     * @return un objet contenant les statistiques agrégées
     */
    NotificationStatsDTO getNotificationStats(Long utilisateurId);
}