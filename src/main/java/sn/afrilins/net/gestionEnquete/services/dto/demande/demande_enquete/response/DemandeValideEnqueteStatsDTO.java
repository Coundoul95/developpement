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
@Schema(name = "DemandeValideEnqueteStats", description = "le modele DemandeValideEnqueteStats")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DemandeValideEnqueteStatsDTO {
    @Schema(description = "Les demandes validées dont l'enquête est en cours")
    long totalEnCours;

    @Schema(description = "Les demandes haute priorité")
    long prioriteHaute;

    @Schema(description = "Les demandes validées dont l'enquête est en validation")
    long enValidation;

    @Schema(description = "Les demandes validées dont l'écheance est passée")
    long enRetard;

    public DemandeValideEnqueteStatsDTO(Number totalEnCours, Number prioriteHaute, Number enValidation, Number enRetard) {
        this.totalEnCours = totalEnCours != null ? totalEnCours.longValue() : 0L;
        this.prioriteHaute = prioriteHaute != null ? prioriteHaute.longValue() : 0L;
        this.enValidation = enValidation != null ? enValidation.longValue() : 0L;
        this.enRetard = enRetard != null ? enRetard.longValue() : 0L;
    }
}
