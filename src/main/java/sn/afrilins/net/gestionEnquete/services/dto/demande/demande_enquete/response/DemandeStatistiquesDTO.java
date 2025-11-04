package sn.afrilins.net.gestionEnquete.services.dto.demande.demande_enquete.response;

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
@Schema(name = "DemandeStatistiques", description = "le modele DemandeStatistiques")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DemandeStatistiquesDTO {
    @Schema(description = "Les demandes validées dont l'enquête est en cours")
    long totalDemandes;

    @Schema(description = "Les demandes validées dont l'enquête est en cours")
    long enCours;

    @Schema(description = "Les demandes validées dont l'enquête est en cours")
    long terminees;

    @Schema(description = "Les demandes validées dont l'enquête est en cours")
    double tauxReussite;
}
