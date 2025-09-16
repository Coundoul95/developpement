package sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import sn.afrilins.net.gestionEnquete.services.dto.demande.demande_enquete.response.DemandeSansEnqueteDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.autre_info.response.AutreInfoDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.conclusion.response.ConclusionDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.source_info.response.SourceInfoDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.DocumentDTO;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "EnqueteAll", description = "Le modèle d'enquête sans sa demande associée")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnqueteAllDTO extends EnqueteDTO{

    @Schema(description = "La demande")
    DemandeSansEnqueteDTO demande;

    @Schema(description = "La liste des autres sources")
    List<AutreInfoDTO> autresInfos;

    @Schema(description = "La liste des sources d'information")
    List<SourceInfoDTO> sourcesInfos;

    @Schema(description = "La liste des conclusions")
    List<ConclusionDTO> conclusions;

    @Schema(description = "La liste des documents")
    Set<DocumentDTO> documents;

}
