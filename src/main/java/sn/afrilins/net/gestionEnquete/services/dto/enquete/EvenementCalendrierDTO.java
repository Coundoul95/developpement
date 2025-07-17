package sn.afrilins.net.gestionEnquete.services.dto.enquete;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.UtilisateurDTO;

import java.time.LocalDate;

@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "evenement_calendrier", description = "le modele evenement_calendrier")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EvenementCalendrierDTO {
    @Schema(description = "L'identifiant du modele")
    Long id;
    @Schema(name = "titre", description = "Le titre")
    String titre;

    @Schema(name = "date", description = "La date")
    LocalDate date;

    @Schema(name = "heure", description = "L'heure")
    String heure;

    @Schema(name = "duree", description = "La durée")
    Integer duree;

    @Schema(name = "priorite", description = "La priorité")
    String priorite;

    @Schema(name = "description", description = "La description")
    String description;

    @Schema(name = "type", description = "Le type")
    TypeEvenementDTO type;

    @Schema(name = "utilisateur", description = "L'utilisateur")
    UtilisateurDTO utilisateur;
}
