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
@Schema(name = "DocumentViewUrlDTO", description = "Modèle représentant les URLs de visualisation et de téléchargement d’un document, ainsi que son type MIME et sa prévisualisabilité.")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentViewUrlDTO {

    @Schema(description = "Identifiant unique du document")
    Long documentId;

    @Schema(description = "URL permettant d'afficher le document dans le navigateur (mode aperçu)")
    String viewUrl;

    @Schema(description = "URL permettant de télécharger le document")
    String downloadUrl;

    @Schema(description = "Type MIME du document (ex. application/pdf, image/png)")
    String contentType;

    @Schema(description = "Indique si le document peut être affiché directement dans le navigateur (PDF, image, vidéo, texte, etc.)")
    boolean canPreview;
}
