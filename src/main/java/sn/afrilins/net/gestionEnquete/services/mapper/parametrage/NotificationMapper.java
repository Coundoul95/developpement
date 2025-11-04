package sn.afrilins.net.gestionEnquete.services.mapper.parametrage;

import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Notification;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.notification.response.NotificationDTO;
import sn.afrilins.net.gestionEnquete.util.EntityMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {

    @Override
    @Mapping(source = "utilisateur.id", target = "utilisateurId")
    @Mapping(source = "createdAt", target = "dateEnvoi")
    NotificationDTO toDto(Notification notification);

}
