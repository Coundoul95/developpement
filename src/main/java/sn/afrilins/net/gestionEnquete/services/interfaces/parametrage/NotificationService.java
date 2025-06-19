package sn.afrilins.net.gestionEnquete.services.interfaces.parametrage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.NotificationDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.NotificationUpdateDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.request.NotificationRequestDTO;

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
     * Récupère une notification selon son identifiant.
     *
     * @param id l'identifiant de la notification
     * @return la notification correspondante, ou {@code null} si elle n'existe pas
     */
    NotificationDTO findNotificationById(Long id);

    /**
     * Récupère toutes les notifications d’un utilisateur de manière paginée.
     *
     * @param pageable les informations de pagination
     * @param utilisateurId l'identifiant de l'utilisateur concerné
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
     * Récupère toutes les notifications non lues pour un utilisateur.
     *
     * @param utilisateurId l'identifiant de l'utilisateur
     * @return une page de notifications non lues
     */
    Page<NotificationDTO> findAllNonLuesByUtilisateurId(Long utilisateurId, String typeNotification, Pageable pageable);

    /**
     * Récupère toutes les notifications lues pour un utilisateur.
     *
     * @param utilisateurId l'identifiant de l'utilisateur
     * @return une page de notifications lues
     */
    Page<NotificationDTO> findAllLuesByUtilisateurId(Long utilisateurId, String typeNotification, Pageable pageable);

}
