package sn.afrilins.net.gestionEnquete.services.dto.parametrage.document.response;

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
@Schema(name = "DocumentUsage", description = "le modele DocumentUsage")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentUsageDTO {
    @Schema(description = "ID du document")
    private Long documentId;

    @Schema(description = "Vrai si le document est utilisé dans au moins une enquête, source ou demande")
    private boolean used;

    @Schema(description = "Message explicatif")
    private String message;
}
