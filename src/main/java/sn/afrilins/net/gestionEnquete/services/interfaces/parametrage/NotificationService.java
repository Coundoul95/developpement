package sn.afrilins.net.gestionEnquete.services.interfaces.parametrage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.NotificationDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.request.NotificationRequestDTO;

public interface NotificationService {

    NotificationDTO createNotification(NotificationRequestDTO notification);

    NotificationDTO updateNotification(NotificationDTO notification);

    void deleteNotification(Long id);

    NotificationDTO findNotificationById(Long id);

    Page<NotificationDTO> readAllNotification(Pageable pageable, Long utilisateurId);

    NotificationDTO markAsReadNotification(Long id);
}
