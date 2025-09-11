package sn.afrilins.net.gestionEnquete.services.dto.enquete.source_info.request;

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
@Schema(name = "source_info_update_request", description = "le modele SourceInfoUpdateRequest")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SourceInfoUpdateRequestDTO {

    @Schema(description = "L'identifiant du modèle")
    Long id;

    @Schema(description = "Le nom", required = true)
    String nom;

    @Schema(description = "La description détaillée")
    String description;

    @Schema(description = "Les commentaires")
    String commentaires;

    @Schema(description = "Le niveau de fiabilité", required = true)
    String niveauFiabilite;

    @Schema(description = "L'identifiant de l'état de la source", required = true)
    Long etatId;

}
