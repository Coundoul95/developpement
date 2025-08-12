package sn.afrilins.net.gestionEnquete.services.dto.enquete;


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
@Schema(name = "conclusion", description = "le modele conclusion")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnqueteDTO {

    @Schema(description = "L'identifiant du modele")
    Long id;

    @Schema(description = "L'état")
    EtatEnqueteDTO etat;

    @Schema(description = "La référence")
    String reference;

    @Schema(description = "La progression")
    int progression;

    @Schema(description = "La date de début")
    LocalDateTime dateDebut;

    @Schema(description = "La date de fin")
    LocalDateTime dateFin;

    @Schema(description = "La date de validation")
    LocalDateTime dateValidation;

    @Schema(description = "La date d'annulation ")
    LocalDateTime dateAnnulation;

    @Schema(description = "La date de création")
    LocalDateTime createdAt;

}
