package sn.afrilins.net.gestionEnquete.services.dto.parametrage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Schema(description = "Le modele centre de gestion", name = "CentreDeGestion")
public class CentreDeGestionDTO {

    @Schema(description = "L'identifiant du modele")
    Long id;

    @Schema(description = "Le code")
    String code;

    @Schema(name = "libelle", description = "le libelle")
    String libelle;
}
