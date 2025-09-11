package sn.afrilins.net.gestionEnquete.services.dto.enquete.calendrier.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "evenement_calendrier_request", description = "le modele evenement_calendrier_request")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EvenementCalendrierRequestDTO {

    @Schema(description = "Le titre", required = true)
    String titre;

    @Schema(description = "La date", required = true)
    LocalDate date;

    @Schema(description = "L'heure", required = true)
    String heure;

    @Schema(description = "La durée", required = true)
    Integer duree;

    @Schema(description = "La priorité", required = true)
    String priorite;

    @Schema(name = "description", description = "La description")
    String description;

    @Schema(description = "Le code du type", required = true)
    String typeCode;

    @Schema(description = "L'identifiant de l'utilisateur lié", required = true)
    Long utilisateurId;
}
