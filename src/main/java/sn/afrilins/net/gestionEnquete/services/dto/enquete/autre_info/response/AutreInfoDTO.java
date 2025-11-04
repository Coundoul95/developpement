package sn.afrilins.net.gestionEnquete.services.dto.enquete.autre_info.response;

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
@Schema(name = "autre_info", description = "le modele autre_info")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AutreInfoDTO {

    @Schema(description = "L'identifiant du modele")
    Long id;

    @Schema(description = "La catégorie")
    String categorie;

    @Schema(description = "L'objet")
    String objet;

    @Schema(description = "La description")
    String description;

    @Schema(description = "L'importance")
    int importance;

    @Schema(description = "l'état de la source")
    EtatAutreInfoDTO etat;

    @Schema(description = "La date d'enregistrement de la source")
    LocalDateTime dateEnregistrement;

    @Schema(description = "La date de création")
    LocalDateTime createdAt;

    @Schema(description = "La date de mis à jour")
    LocalDateTime updatedAt;
}
