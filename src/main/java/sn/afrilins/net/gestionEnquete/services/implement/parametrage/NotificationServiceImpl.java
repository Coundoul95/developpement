package sn.afrilins.net.gestionEnquete.services.implement.parametrage;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.NotificationDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.request.NotificationRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.NotificationService;

@AllArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PACKAGE)
@Transactional
public class NotificationServiceImpl implements NotificationService {



    /**
     * @param notification
     * @return
     */
    @Override
    public NotificationDTO createNotification(NotificationRequestDTO notification) {
        return null;
    }

    /**
     * @param notification
     * @return
     */
    @Override
    public NotificationDTO updateNotification(NotificationDTO notification) {
        return null;
    }

    /**
     * @param id
     */
    @Override
    public void deleteNotification(Long id) {

    }

    /**
     * @param id
     * @return
     */
    @Override
    public NotificationDTO findNotificationById(Long id) {
        return null;
    }

    /**
     * @param pageable
     * @param utilisateurId
     * @return
     */
    @Override
    public Page<NotificationDTO> readAllNotification(Pageable pageable, Long utilisateurId) {
        return null;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public NotificationDTO markAsReadNotification(Long id) {
        return null;
    }
}
