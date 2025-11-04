package sn.afrilins.net.gestionEnquete.services.dto.enquete.autre_info.request;

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
@Schema(name = "AutreInfoRequest", description = "le modele AutreInfoRequest")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AutreInfoRequestDTO {

    @Schema(description = "La catégorie")
    String categorie;

    @Schema(description = "L'objet de la source")
    String objet;

    @Schema(description = "La description")
    String description;

    @Schema(description = "L'importance")
    Integer importance;

    @Schema(description = "le code l'état")
    String codeEtat;

    @Schema(description = "La date d'enregistrement de la source (optionnelle)")
    LocalDateTime dateEnregistrement; // peut être null

    @Schema(description = "L'identifiant de l'enquête")
    Long enqueteId;

}
