package sn.afrilins.net.gestionEnquete.services.dto.enquete.request;

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
@Schema(name = "autre_info_request", description = "le modele autre_info_request")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AutreInfoRequestDTO {

    @Schema(description = "La catégorie")
    String categorie;

    @Schema(description = "La description")
    String description;

    @Schema(description = "L'importance")
    String importance;

}
