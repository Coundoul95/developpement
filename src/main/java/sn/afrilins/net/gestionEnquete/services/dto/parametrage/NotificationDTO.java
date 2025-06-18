package sn.afrilins.net.gestionEnquete.services.dto.parametrage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "notification", description = "le modele notification")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationDTO {

    @Schema(description = "L'identifiant de la notification")
    Long id;

    @Schema(description = "Le message de la notification")
    String message;

    @Schema(description = "Statut de lecture de la notification")
    Boolean lu;

    @Schema(description = "Date d'envoi de la notification")
    LocalDateTime dateEnvoi;

    @Schema(description = "Date d'envoi de la notification")
    LocalDateTime dateLu;

    @Schema(description = "Type de la notification")
    String typeNotification;

    @Schema(description = "Identifiant de l'utilisateur destinataire")
    Long utilisateurId;

}
