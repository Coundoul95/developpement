package sn.afrilins.net.gestionEnquete.services.dto.parametrage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "notification_update", description = "le modele notification")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationUpdateDTO {

    @Schema(description = "Le message de la notification")
    Optional<String>  message;

    @Schema(description = "Type de la notification")
    Optional<String> typeNotification;

}
