package sn.afrilins.net.gestionEnquete.services.dto.enquete.source_info.response;

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
@Schema(name = "type_source", description = "le modele type_source")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeSourceDTO {

    @Schema(description = "L'identifiant du modele")
    Long id;

    @Schema(name = "code", description = "Le code")
    String code;

    @Schema(name = "libelle", description = "Le libellé")
    String libelle;

}
