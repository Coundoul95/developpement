package sn.afrilins.net.gestionEnquete.services.implement.parametrage;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Notification;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Utilisateur;
import sn.afrilins.net.gestionEnquete.exception.BadRequestAlertException;
import sn.afrilins.net.gestionEnquete.exception.CustomBadRequestException;
import sn.afrilins.net.gestionEnquete.repository.UtilisateurRepository;
import sn.afrilins.net.gestionEnquete.repository.parametrage.NotificationRepository;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.NotificationDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.NotificationStatsDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.NotificationUpdateDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.request.NotificationRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.NotificationService;
import sn.afrilins.net.gestionEnquete.services.mapper.parametrage.NotificationMapper;
import sn.afrilins.net.gestionEnquete.util.ValidationUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PACKAGE)
@Transactional
public class NotificationServiceImpl implements NotificationService {

    final NotificationRepository notificationRepository;

    final UtilisateurRepository utilisateurRepository;

    final NotificationMapper notificationMapper;


    @Override
    public NotificationDTO createNotification(NotificationRequestDTO notification) {

        ValidationUtils.requireNonNull(notification.getUtilisateurId(), "utilisateur_id", "notification");
        ValidationUtils.requireNonBlank(notification.getMessage(), "message", "notification");
        ValidationUtils.requireNonBlank(notification.getTypeNotification(), "type_notification", "notification");

        Utilisateur utilisateur = utilisateurRepository.findById(notification.getUtilisateurId())
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("utilisateur_existe_pas", "utilisateur",
                                "utilisateur_existe_pas"))  );

        Notification entity = Notification.builder()
                .message(notification.getMessage())
                .typeNotification(notification.getTypeNotification())
                .utilisateur(utilisateur)
                .urgent(notification.getUrgent() || false)
                .dateLu(null)
                .createdAt(LocalDateTime.now())
                .build();

        entity = notificationRepository.save(entity);
        System.out.println(entity.getUtilisateur().getId());
        return notificationMapper.toDto(entity);
    }


    @Override
    public NotificationDTO updateNotification(Long id, NotificationUpdateDTO notification) {
        ValidationUtils.requireNonNull(id.toString(), "notification_id", "notification");

        Notification entity = notificationRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("notification_introuvable", "notification", "id_not_found")));

        if (notification.getMessage().isPresent()) {
            entity.setMessage(String.valueOf(notification.getMessage()));
        }
        if (notification.getTypeNotification().isPresent()) {
            entity.setTypeNotification(String.valueOf(notification.getTypeNotification()));
        }

        return notificationMapper.toDto(notificationRepository.save(entity));
    }


    @Override
    public void deleteNotification(Long id) {
        Notification entity = notificationRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("notification_introuvable", "notification", "id_not_found")));
        notificationRepository.delete(entity);
    }


    @Override
    public NotificationDTO findNotificationById(Long id) {
        return notificationMapper.toDto(findById(id));
    }


    @Override
    public Page<NotificationDTO> readAllNotification(Long utilisateurId, String typeNotification, Pageable pageable) {
        if (utilisateurId != null) {
            ValidationUtils.requirePositiveId(utilisateurId, "utilisateur_id", "notification");
        }
        return notificationRepository.findAllNotification(utilisateurId, typeNotification, pageable)
                .map(notificationMapper::toDto);
    }


    @Override
    public NotificationDTO markAsReadNotification(Long id) {
        Notification existingNotification = findById(id);
        if (existingNotification.getLu()) {
            throw new CustomBadRequestException(
                    new BadRequestAlertException("notification_est_lu", "notification", "notification_est_lu"));
        }
        existingNotification.setDateLu(LocalDateTime.now());
        existingNotification.setLu(true);
        notificationRepository.save(existingNotification);
        return notificationMapper.toDto(existingNotification);
    }

    @Override
    public List<NotificationDTO> markManyAsReadNotification(List<Long> ids) {
        List<Notification> notifications = notificationRepository.findAllById(ids);

        for (Notification notification : notifications) {
            notification.setLu(true);
            notification.setDateLu(LocalDateTime.now());
        }

        List<Notification> updatedNotifications = notificationRepository.saveAll(notifications);

        return updatedNotifications.stream()
                .map(this.notificationMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public Page<NotificationDTO> findAllNonLuesByUtilisateurId(Long utilisateurId, String typeNotification,
            Pageable pageable) {
        if (utilisateurId != null) {
            ValidationUtils.requirePositiveId(utilisateurId, "utilisateur_id", "notification");
        }
        return notificationRepository.findAllNonLuesByUtilisateurId(utilisateurId, typeNotification, pageable)
                .map(notificationMapper::toDto);
    }


    @Override
    public Page<NotificationDTO> findAllLuesByUtilisateurId(Long utilisateurId, String typeNotification,
            Pageable pageable) {
        if (utilisateurId != null) {
            ValidationUtils.requirePositiveId(utilisateurId, "utilisateur_id", "notification");
        }
        return notificationRepository.findAllLuesByUtilisateurId(utilisateurId, typeNotification, pageable)
                .map(notificationMapper::toDto);
    }


    @Override
    public Page<NotificationDTO> findAllUrgentByUtilisateurId(Long utilisateurId, String typeNotification, Pageable pageable) {
        return notificationRepository.findAllUrgentByUtilisateurId(utilisateurId, typeNotification, pageable).map(notificationMapper::toDto);
    }


    @Override
    public void deleteManyNotification(List<Long> ids) {
        for (Long id : ids) {
            notificationRepository.deleteById(id);
        }
    }


    @Override
    public NotificationStatsDTO getNotificationStats(Long utilisateurId) {
        long total = notificationRepository.countByUtilisateurId(utilisateurId);
        long unread = notificationRepository.countByUtilisateurIdAndLuFalse(utilisateurId);
        long read = notificationRepository.countByUtilisateurIdAndLuTrue(utilisateurId);
        long urgent = notificationRepository.countByUtilisateurIdAndUrgentTrue(utilisateurId);

        return NotificationStatsDTO.builder()
                .read(read)
                .unread(unread)
                .urgent(urgent)
                .total(total)
                .build();
    }

    private Notification findById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("notification_introuvable", "notification", "id_not_found")));
    }
}
