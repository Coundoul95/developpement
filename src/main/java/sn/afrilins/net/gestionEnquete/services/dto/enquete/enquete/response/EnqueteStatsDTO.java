package sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.response;

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
@Schema(name = "EnqueteStatistique", description = "Modèle représentant les statistiques des enquêtes")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnqueteStatsDTO {

    @Schema(description = "Les enquêtes en attente de démarrage")
    long enAttente;

    @Schema(description = "Les enquêtes en cours de traitement")
    long enCours;

    @Schema(description = "Les enquêtes terminées")
    long terminees;

    @Schema(description = "Les enquêtes en validation")
    long enValidation;

    @Schema(description = "Les enquêtes en révision")
    long enRevision;

    @Schema(description = "Les enquêtes validées")
    long valides;

    @Schema(description = "Les enquêtes annulées")
    long annulees;

    @Schema(description = "Les enquêtes dont l’échéance est proche")
    long echeances;

    @Schema(description = "Le total des enquêtes")
    long total;

    @Schema(description = "Les enquêtes urgentes")
    long urgent;

    @Schema(description = "La progession total des enquêtes")
    Double progression;
}