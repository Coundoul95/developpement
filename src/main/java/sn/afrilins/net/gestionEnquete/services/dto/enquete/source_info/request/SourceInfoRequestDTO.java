package sn.afrilins.net.gestionEnquete.services.dto.enquete.source_info.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "source_info_request", description = "le modele SourceInfoRequest")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SourceInfoRequestDTO {

    @Schema(description = "Le nom", required = true)
    String nom;

    @Schema(description = "La description détaillée")
    String description;

    @Schema(description = "Les commentaires")
    String commentaires;

    @Schema(description = "Le niveau de fiabilité", required = true)
    int fiabilite;

    @Schema(description = "Le code de l'état de la source", required = true)
    String codeEtat;

    @Schema(description = "Le code du type de la source", required = true)
    String codeType;

    @Schema(description = "L'identifiant de l'utilisateur lié", required = true)
    Long utilisateurId;

    @Schema(description = "La date d'obtention de la source")
    LocalDateTime dateObtention;

    @Schema(description = "La date de mis à jour de la source")
    LocalDateTime dateMiseAJour;

    @Schema(description = "Liste des identifiants des documents associés")
    List<Long> documentIds;

    @Schema(description = "Liste des identifiants des enquêtes associés")
    List<Long> enqueteIds;
}
