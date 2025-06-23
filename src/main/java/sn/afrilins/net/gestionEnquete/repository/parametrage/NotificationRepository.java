package sn.afrilins.net.gestionEnquete.repository.parametrage;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Notification;
import sn.afrilins.net.gestionEnquete.domain.parametrage.QNotification;

public interface NotificationRepository extends JpaRepository<Notification, Long>, QuerydslPredicateExecutor<Notification> {

    // Nombre total de notifications pour un utilisateur
    long countByUtilisateurId(Long utilisateurId);

    // Nombre de notifications lues
    long countByUtilisateurIdAndLuTrue(Long utilisateurId);

    // Nombre de notifications non lues
    long countByUtilisateurIdAndLuFalse(Long utilisateurId);

    // Nombre de notifications urgentes
    long countByUtilisateurIdAndUrgentTrue(Long utilisateurId);

    // Récupérer toutes les notifications urgentes d’un utilisateur avec pagination
    default Page<Notification> findAllUrgentByUtilisateurId(Long utilisateurId, String type, Pageable pageable){
        QNotification qNotification = QNotification.notification;
        BooleanBuilder builder = new BooleanBuilder();
        if (utilisateurId != null) {
            builder.and(qNotification.utilisateur.id.eq(utilisateurId));
        }
        if (type != null) {
            builder.and(qNotification.typeNotification.eq(type));
        }
        builder.and(qNotification.urgent.isTrue());
        return findAll(builder, pageable);
    }

    // Récupérer toutes les notifications d’un utilisateur avec pagination
    default Page<Notification> findAllNotification(Long utilisateurId, String type, Pageable pageable) {
        QNotification qNotification = QNotification.notification;
        BooleanBuilder builder = new BooleanBuilder();

        if (utilisateurId != null) {
            builder.and(qNotification.utilisateur.id.eq(utilisateurId));
        }

        if (type != null) {
            builder.and(qNotification.typeNotification.eq(type));
        }

        return findAll(builder, pageable);
    }


    // Récupérer toutes les notifications non lues d’un utilisateur avec pagination
    default Page<Notification> findAllNonLuesByUtilisateurId(Long utilisateurId, String type, Pageable pageable) {
        QNotification qNotification = QNotification.notification;
        BooleanBuilder builder = new BooleanBuilder();

        if (utilisateurId != null) {
            builder.and(qNotification.utilisateur.id.eq(utilisateurId));
        }

        if (type != null) {
            builder.and(qNotification.typeNotification.eq(type));
        }


        builder.and(qNotification.lu.isFalse());
        return findAll(builder, pageable);
    }

    // Récupérer les notifications lues d’un utilisateur avec pagination
    default Page<Notification> findAllLuesByUtilisateurId(Long utilisateurId, String type,  Pageable pageable) {
        QNotification qNotification = QNotification.notification;
        BooleanBuilder builder = new BooleanBuilder();

        if (utilisateurId != null) {
            builder.and(qNotification.utilisateur.id.eq(utilisateurId));
        }

        if (type != null) {
            builder.and(qNotification.typeNotification.eq(type));
        }

        builder.and(qNotification.lu.isTrue());

        return findAll(builder, pageable);
    }

}
