package sn.afrilins.net.gestionEnquete.services.dto.enquete.conclusion.response;

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
public class ConclusionDTO {

    @Schema(description = "L'identifiant du modele")
    Long id;

    @Schema(description = "Le titre")
    String titre;

    @Schema(description = "Le contenu")
    String contenu;

    @Schema(description = "La mesure à suivre")
    String mesuresSuivi;

    @Schema(description = "La recommandation")
    String recommandation;

    @Schema(description = "La date de validation")
    LocalDateTime dateValidation;
}
