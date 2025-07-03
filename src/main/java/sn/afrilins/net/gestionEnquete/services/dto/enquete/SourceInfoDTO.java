package sn.afrilins.net.gestionEnquete.services.dto.enquete;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.DocumentDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.UtilisateurDTO;

import java.time.LocalDateTime;
import java.util.List;

@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "source_info", description = "le modele SourceInfo")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SourceInfoDTO {

    @Schema(description = "L'identifiant du modèle")
    Long id;

    @Schema(description = "Le nom")
    String nom;

    @Schema(description = "La description détaillée")
    String description;

    @Schema(description = "Les commentaires")
    String commentaires;

    @Schema(description = "Le niveau de fiabilité")
    String niveauFiabilite;

    @Schema(description = "L'état de la source d'information")
    EtatSourceInfoDTO etat;

    @Schema(description = "Le type de la source d'information")
    TypeSourceDTO type;

    @Schema(description = "L'utilisateur associé")
    UtilisateurDTO utilisateur;

    @Schema(description = "Liste des documents associés")
    List<DocumentDTO> documents;

    @Schema(description = "Date de création", example = "2024-06-30T12:45:30")
    LocalDateTime dateObtention;

    @Schema(description = "Date de mis à jour", example = "2024-06-30T12:45:30")
    LocalDateTime dateMiseAJour;

}
