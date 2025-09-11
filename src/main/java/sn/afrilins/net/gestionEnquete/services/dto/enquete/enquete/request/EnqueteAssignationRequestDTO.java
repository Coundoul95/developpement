package sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.request;

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
@Schema(name = "EnqueteAssignationRequest", description = "Le modèle d'assignation d'enquêteur")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnqueteAssignationRequestDTO {

    @Schema(description = "L'identifiant de l'enquêteur")
    Long enqueteurId;

    @Schema(description = "Les instructions du chef d'enquêteur")
    String instruction;

    @Schema(description = "La date limite pour terminée l'enquête", type = "string")
    LocalDateTime dateLimite;
}
