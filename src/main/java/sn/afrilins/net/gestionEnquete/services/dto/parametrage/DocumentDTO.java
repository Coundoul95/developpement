package sn.afrilins.net.gestionEnquete.services.dto.parametrage;

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
@Schema(name = "document", description = "le modele notification")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentDTO {
    @Schema(description = "L'identifiant de la notification")
    Long id;

    @Schema(description = "Le nom du document")
    String nom;

    @Schema(description = "La desccription du document")
    String description;

    @Schema(description = "Le chemin du document")
    String chemin;

    @Schema(description = "L'extension du document")
    String extension;

    @Schema(description = "La taille du document")
    int taille;

    @Schema(description = "La version du document")
    int version;

    @Schema(description = "Le type de document")
    TypeDocumentDTO type;
}
