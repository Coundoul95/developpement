package sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.UtilisateurDTO;

import java.time.LocalDateTime;

@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Enquete", description = "Le modèle d'enquête sans sa demande associée")
@FieldDefaults(level = AccessLevel.PROTECTED)
public class EnqueteDTO {

    @Schema(description = "L'identifiant du modele")
    Long id;

    @Schema(description = "L'état")
    EtatEnqueteDTO etat;

    @Schema(description = "La référence")
    String reference;

    @Schema(description = "La progression")
    int progression;

    @Schema(description = "Les instructions du chef d'enquêteur")
    String instruction;

    @Schema(description = "La date de début", type = "string", format = "date-time", example = "2025-08-12T15:30:00")
    LocalDateTime dateDebut;

    @Schema(description = "La date limite pour terminée l'enquête", type = "string", format = "date-time", example = "2025-08-12T15:30:00")
    LocalDateTime dateLimite;

    @Schema(description = "La date de fin", type = "string", format = "date-time", example = "2025-08-12T15:30:00")
    LocalDateTime dateFin;

    @Schema(description = "La date de validation", type = "string", format = "date-time", example = "2025-08-12T15:30:00")
    LocalDateTime dateValidation;

    @Schema(description = "La date d'annulation ", type = "string", format = "date-time", example = "2025-08-12T15:30:00")
    LocalDateTime dateAnnulation;

    @Schema(description = "La date d'assignation ", type = "string", format = "date-time", example = "2025-08-12T15:30:00")
    LocalDateTime dateAssignation;

    @Schema(description = "La date de création", type = "string", format = "date-time", example = "2025-08-12T15:30:00")
    LocalDateTime createdAt;

    @Schema(description = "La date de mis à jour", type = "string", format = "date-time", example = "2025-08-12T15:30:00")
    LocalDateTime updatedAt;

    @Schema(description = "L'enquêteur charger de l'enquête")
    UtilisateurDTO enqueteur;

}
