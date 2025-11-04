package sn.afrilins.net.gestionEnquete.services.dto.parametrage.document.request;

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
@Schema(name = "DocumentUploadRequest", description = "le modele DocumentUploadRequest")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentUploadRequestDTO {

    @Schema(description = "Le nom du document")
    String nom;

    @Schema(description = "La desccription du document")
    String description;

    @Schema(description = "Le code du type de document")
    String codeType;
}
