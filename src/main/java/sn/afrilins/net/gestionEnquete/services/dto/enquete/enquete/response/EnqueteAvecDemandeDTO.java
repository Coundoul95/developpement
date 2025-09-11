package sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import sn.afrilins.net.gestionEnquete.services.dto.demande.demande_enquete.response.DemandeSansEnqueteDTO;

@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "EnqueteAvecDemande", description = "Le modèle d'enquête avec sa demande associée")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnqueteAvecDemandeDTO extends EnqueteDTO {

    @Schema(description = "La demande")
    DemandeSansEnqueteDTO demande;
}
