package sn.afrilins.net.gestionEnquete.services.dto.parametrage.notification.response;

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
@Schema(name = "notification_stats", description = "le modele notification")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationStatsDTO {
    @Schema(description = "Le nombre total de notification de l'utilisateur")
    private long total;

    @Schema(description = "Le nombre total de notification non lu de l'utilisateur")
    private long unread;

    @Schema(description = "Le nombre total de notification lu de l'utilisateur")
    private long read;

    @Schema(description = "Le nombre total de notification urgent de l'utilisateur")
    private long urgent;
}
