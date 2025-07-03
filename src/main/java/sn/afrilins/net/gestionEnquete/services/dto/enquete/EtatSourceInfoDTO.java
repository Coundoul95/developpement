package sn.afrilins.net.gestionEnquete.services.dto.enquete;


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
@Schema(name = "etat_source_info", description = "le modele EtatSourceInfo")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EtatSourceInfoDTO {

    @Schema(description = "L'identifiant du modele")
    Long id;

    @Schema(name = "code", description = "Le code")
    String code;

    @Schema(name = "libelle", description = "Le libellé")
    String libelle;

}
