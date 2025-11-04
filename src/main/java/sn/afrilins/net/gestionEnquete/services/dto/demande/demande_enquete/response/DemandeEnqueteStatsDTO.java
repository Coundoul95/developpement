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
@Schema(name = "DemandeEnqueteStats", description = "le modele DemandeEnqueteStats")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DemandeEnqueteStatsDTO {
    @Schema(description = "Les demandes validées")
    long validees;

    @Schema(description = "Les demandes en attentes")
    long enAttentes;

    @Schema(description = "Les demandes rejetées")
    long rejetees;

    @Schema(description = "Les demandes annulées")
    long annulees;

    @Schema(description = "Les demandes en complément")
    long enComplement;

    @Schema(description = "Total demandes")
    long total;
}
