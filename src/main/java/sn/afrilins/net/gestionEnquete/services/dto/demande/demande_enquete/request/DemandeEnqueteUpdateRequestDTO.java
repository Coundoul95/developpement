package sn.afrilins.net.gestionEnquete.services.dto.demande.demande_enquete.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "DemandeEnqueteUpdateRequest", description = "le modele DemandeEnqueteUpdateRequest")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DemandeEnqueteUpdateRequestDTO {

    @Schema(name = "objet", description = "L'objet")
    Optional<String> objet;

    @Schema(name = "description", description = "La description")
    Optional<String> description;

    @Schema(name = "urgent", description = "L'urgent")
    Optional<Boolean> urgent;

    @Schema(name = "priorite", description = "La priorité")
    Optional<Integer> priorite;

    @Schema(name = "dateEcheance", description = "La date écheance")
    Optional<LocalDateTime> dateEcheance;

    @Schema(name = "commentaireValidation", description = "La commentaire de validation")
    Optional<String> commentaireValidation;

    @Schema(name = "concerneId", description = "Le concerné")
    Optional<Long> concerneId;

    @Schema(description = "Liste des identifiants des documents associés")
    List<Long> documentIds;

}
