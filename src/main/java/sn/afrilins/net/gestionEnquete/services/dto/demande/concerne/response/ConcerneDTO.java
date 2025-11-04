package sn.afrilins.net.gestionEnquete.services.dto.demande.concerne.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import sn.afrilins.net.gestionEnquete.domain.enume.TypeConcerne;

@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "concerne_request", description = "le modele concerne_request")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConcerneDTO {

    @Schema(description = "L'identifiant du modele")
    Long id;

    @Schema(name = "type", description = "Le type ")
    TypeConcerne type;

    @Schema(name = "telephone", description = "Le numéro")
    String telephone;

    @Schema(name = "regionSocial", description = "La region socila")
    String regionSocial;

}
