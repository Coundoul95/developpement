package sn.afrilins.net.gestionEnquete.services.dto.parametrage.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "notification_request", description = "le modele notification_request")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationRequestDTO {

    @Schema(description = "Le message de la notification")
    String message;

    @Schema(description = "Type de la notification")
    String typeNotification;

    @Schema(description = "Identifiant de l'utilisateur destinataire")
    Long utilisateurId;

}
