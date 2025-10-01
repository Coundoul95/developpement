package sn.afrilins.net.gestionEnquete.services.dto.parametrage.document.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;


@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "DocumentAssociationRequest", description = "le modele DocumentAssociationRequest")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentAssociationRequestDTO {

    @Schema(description = "La liste des informations des documents")
    List<DocumentUploadRequestDTO> infos;
}
