package sn.afrilins.net.gestionEnquete.services.dto.demande;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.EnqueteDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.UtilisateurDTO;

import java.time.LocalDateTime;

@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "demande_enquete", description = "le modele demande_enquete")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DemandeEnqueteDTO {

    @Schema(description = "L'identifiant du model")
    Long id;

    @Schema(name = "objet", description = "L'objet")
    String objet;

    @Schema(name = "description", description = "La description")
    String description;

    @Schema(name = "urgent", description = "L'urgent")
    Boolean urgent;

    @Schema(name = "commentaireValidation", description = "Le commentaire de validation")
    String commentaireValidation;

    @Schema(name = "priorite", description = "La priorité")
    int priorite;

    @Schema(name = "dateEcheance", description = "La date écheance")
    LocalDateTime dateEcheance;

    @Schema(name = "dateValidation", description = "La date de validation")
    LocalDateTime dateValidation;

    @Schema(name = "dateAnnulation", description = "La date d'annulation")
    LocalDateTime dateAnnulation;

    @Schema(name = "etat", description = "L'état")
    EtatDemandeDTO etat;

    @Schema(name = "enquete", description = "L'état")
    EnqueteDTO enquete;

    @Schema(name = "concerne", description = "Le concerné")
    ConcerneDTO concerne;

    @Schema(name = "utilisateur", description = "L'utilisateur")
    UtilisateurDTO utilisateur;

}
