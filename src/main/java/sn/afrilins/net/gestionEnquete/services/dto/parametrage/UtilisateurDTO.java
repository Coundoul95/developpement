package sn.afrilins.net.gestionEnquete.services.dto.parametrage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "utilisateur", description = "le modele utilisateur")
@FieldDefaults(level = AccessLevel.PRIVATE)
public  class UtilisateurDTO {

    @Schema(name = "id", description = "l'identifiant de l'utilisateur")
    Long id;

    @Schema(name = "username")
    String username;
}
