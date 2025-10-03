package sn.afrilins.net.gestionEnquete.services.dto.enquete.source_info.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Optional;

@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "source_info_update_request", description = "le modele SourceInfoUpdateRequest")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SourceInfoUpdateRequestDTO {


    @Schema(description = "Le nom (optionnel)")
    Optional<String> nom;

    @Schema(description = "La description détaillée (optionnelle)")
    Optional<String> description;

    @Schema(description = "Les commentaires (optionnels)")
    Optional<String> commentaires;

    @Schema(description = "Le niveau de fiabilité (optionnel)")
    Optional<Integer> fiabilite;

    @Schema(description = "L'identifiant de l'état de la source (optionnel)")
    Optional<String> codeEtat;

    @Schema(description = "Liste des identifiants des documents associés")
    List<Long> documentIds;

    @Schema(description = "Liste des identifiants des enquêtes associés")
    List<Long> enqueteIds;

}
