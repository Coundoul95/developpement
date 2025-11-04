package sn.afrilins.net.gestionEnquete.services.dto.demande.demande_enquete.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import sn.afrilins.net.gestionEnquete.services.dto.demande.concerne.request.ConcerneRequestDTO;

import java.time.LocalDateTime;
import java.util.List;

@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "demande_enquete_request", description = "le modele demande_enquete_request")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DemandeEnqueteRequestDTO {

    @Schema(name = "objet", description = "L'objet")
    String objet;

    @Schema(name = "description", description = "La description")
    String description;

    @Schema(name = "urgent", description = "L'urgent")
    Boolean urgent;

    @Schema(name = "priorite", description = "La priorité")
    int priorite;

    @Schema(name = "dateEcheance", description = "La date écheance")
    LocalDateTime dateEcheance;

    @Schema(name = "concerneId", description = "Le concerné")
    Long concerneId;

    @Schema(name = "concerne", description = "Le concerné à créer s'il n'existe pas déjà")
    ConcerneRequestDTO concerne;

    @Schema(name = "utilisateurId", description = "L'utilisateur")
    Long utilisateurId;

    @Schema(description = "Liste des identifiants des documents associés")
    List<Long> documentIds;

}
