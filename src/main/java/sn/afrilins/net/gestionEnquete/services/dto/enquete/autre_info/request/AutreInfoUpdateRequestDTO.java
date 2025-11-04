package sn.afrilins.net.gestionEnquete.services.dto.enquete.autre_info.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Optional;

@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "AutreInfoUpdateRequest", description = "Le modèle de mise à jour d'une AutreInfo")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AutreInfoUpdateRequestDTO {

    @Schema(description = "La catégorie (optionnelle)")
    Optional<String> categorie = Optional.empty();

    @Schema(description = "L'objet de la source (optionnel)")
    Optional<String> objet = Optional.empty();

    @Schema(description = "La description (optionnelle)")
    Optional<String> description = Optional.empty();

    @Schema(description = "L'importance (optionnelle)")
    Optional<Integer> importance = Optional.empty();

    @Schema(description = "Le code de l'état (optionnel)")
    Optional<String> codeEtat = Optional.empty();

    @Schema(description = "La date d'enregistrement (optionnelle)")
    Optional<LocalDateTime> dateEnregistrement = Optional.empty();
}
