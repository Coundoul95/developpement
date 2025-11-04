package sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.request;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "EnqueteDocumentRequest", description = "Le modèle EnqueteDocumentRequest")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnqueteDocumentRequestDTO {

    @Schema(description = "Liste des identifiants des documents déjà existants à associer à l'enquête")
    List<Long> ids;

}
