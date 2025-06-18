package sn.afrilins.net.gestionEnquete.repository.parametrage;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Notification;
import sn.afrilins.net.gestionEnquete.domain.parametrage.QNotification;
import sn.afrilins.net.gestionEnquete.domain.parametrage.TypeDocument;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long>, QuerydslPredicateExecutor<Notification> {

    // Récupérer toutes les notifications d’un utilisateur avec pagination
    default Page<Notification> findAllNotifcation(Pageable pageable, Long utilisateurId) {
        QNotification qNotification = QNotification.notification;
        BooleanBuilder builder = new BooleanBuilder();

        if (utilisateurId != null) {
            builder.and(qNotification.utilisateur.id.eq(utilisateurId));
        }

        return findAll(builder, pageable);
    }

    // Récupérer toutes les notifications non lues d’un utilisateur
    default List<Notification> findAllNonLuesByUtilisateurId(Long utilisateurId) {
        QNotification qNotification = QNotification.notification;
        BooleanBuilder builder = new BooleanBuilder();

        if (utilisateurId != null) {
            builder.and(qNotification.utilisateur.id.eq(utilisateurId));
            builder.and(qNotification.lu.eq(false));
        }

        return (List<Notification>) findAll(builder);
    }

    // Récupérer les notifications lues d’un utilisateur
    default List<Notification> findAllLuesByUtilisateurId(Long utilisateurId) {
        QNotification qNotification = QNotification.notification;
        BooleanBuilder builder = new BooleanBuilder();

        if (utilisateurId != null) {
            builder.and(qNotification.utilisateur.id.eq(utilisateurId));
            builder.and(qNotification.lu.eq(true));
        }

        return (List<Notification>) findAll(builder);
    }

    // Récupérer les notifications par type pour un utilisateur
    default List<Notification> findByTypeNotificationAndUtilisateurId(String type, Long utilisateurId) {
        QNotification qNotification = QNotification.notification;
        BooleanBuilder builder = new BooleanBuilder();

        if (utilisateurId != null && type != null) {
            builder.and(qNotification.utilisateur.id.eq(utilisateurId));
            builder.and(qNotification.typeNotification.eq(type));
        }

        return (List<Notification>) findAll(builder);
    }




}
