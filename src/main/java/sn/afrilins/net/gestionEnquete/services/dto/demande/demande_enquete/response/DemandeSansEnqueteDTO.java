package sn.afrilins.net.gestionEnquete.services.dto.demande.demande_enquete.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import sn.afrilins.net.gestionEnquete.services.dto.demande.concerne.response.ConcerneDTO;
import sn.afrilins.net.gestionEnquete.services.dto.demande.etat_demande.response.EtatDemandeDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.DocumentDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.UtilisateurDTO;

import java.time.LocalDateTime;
import java.util.List;

@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "demande_enquete", description = "le modele demande_enquete")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DemandeSansEnqueteDTO {

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

    @Schema(description = "La référence")
    String reference;

    @Schema(name = "dateEcheance", description = "La date écheance")
    LocalDateTime dateEcheance;

    @Schema(name = "dateValidation", description = "La date de validation")
    LocalDateTime dateValidation;

    @Schema(name = "dateAnnulation", description = "La date d'annulation")
    LocalDateTime dateAnnulation;

    @Schema(name = "etat", description = "L'état")
    EtatDemandeDTO etat;

    @Schema(name = "concerne", description = "Le concerné")
    ConcerneDTO concerne;

    @Schema(name = "utilisateur", description = "L'utilisateur")
    UtilisateurDTO utilisateur;

    @Schema(name = "createdAt", description = "La date de création")
    LocalDateTime createdAt;

    @Schema(name = "updatedAt", description = "La date de mis à jour")
    LocalDateTime updatedAt;

    @Schema(name= "documents", description = "La liste des documents")
    List<DocumentDTO> documents;
}
