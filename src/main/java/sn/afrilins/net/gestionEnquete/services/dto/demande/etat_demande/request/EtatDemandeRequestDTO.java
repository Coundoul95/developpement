package sn.afrilins.net.gestionEnquete.services.dto.demande.etat_demande.request;

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
@Schema(name = "etat_demande_request", description = "le modele etat_demande_request")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EtatDemandeRequestDTO {

    @Schema(name = "code", description = "Le code")
    String code;

    @Schema(name = "libelle", description = "Le libellé")
    String libelle;
}
