package sn.afrilins.net.gestionEnquete.services.dto.parametrage.document.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;



@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "type_document", description = "le modele type_document")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeDocumentDTO {

    @Schema(description = "L'identifiant du modele")
    Long id;

    @Schema(name = "code", description = "Le code")
    String code;

    @Schema(name = "libelle", description = "Le libellé")
    String libelle;
}
