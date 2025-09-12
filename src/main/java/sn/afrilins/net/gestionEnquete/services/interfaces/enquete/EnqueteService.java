package sn.afrilins.net.gestionEnquete.services.interfaces.enquete;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.afrilins.net.gestionEnquete.domain.enume.TypeConcerne;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.response.EnqueteAllDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.response.EnqueteAvecDemandeDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.request.EnqueteAssignationRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.response.EnqueteDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.response.EnqueteStatsDTO;

import java.time.LocalDateTime;

public interface EnqueteService {

    /**
     * Crée une nouvelle enquete.
     *
     * @param demandeId l'identifiant de la demande
     * @return la conclusion créée
     */
    EnqueteDTO createEnquete(Long demandeId);

    /**
     * Met à jour une enquête existante.
     *
     * @param dto les nouvelles données de l'enquête
     * @return l'enquête mise à jour
     */
    EnqueteDTO updateEnquete(EnqueteDTO dto);

    /**
     * Supprime une enquête par son identifiant.
     *
     * @param id l’identifiant de l'enquête à supprimer
     */
    void deleteEnquete(Long id);

    /**
     * Recherche une enquête par son identifiant.
     *
     * @param id l’identifiant de la conclusion
     * @return la enquete trouvée
     */
    EnqueteAvecDemandeDTO findEnqueteById(Long id);

    /**
     * Récupère une page de conclusions.
     *
     * @param pageable les informations de pagination
     * @return une page de conclusions
     */
    Page<EnqueteAvecDemandeDTO> readAllEnqueteAvecDemande(String etatCode,
                                                          Integer progression,
                                                          LocalDateTime dateDebut,
                                                          LocalDateTime dateFin,
                                                          Boolean assignee,      // filtrer assignée/non assignée
                                                          Long enqueteurId,      // filtrer par enquêteur
                                                          String search,
                                                          String type,
                                                          Integer priorite,
                                                          Boolean urgent,
                                                          Pageable pageable);

    Page<EnqueteDTO> readAllEnqueteSansDemande(String etatCode,
                                               Integer progression,
                                               LocalDateTime dateDebut,
                                               LocalDateTime dateFin,
                                               Boolean assignee,      // filtrer assignée/non assignée
                                               Long enqueteurId,      // filtrer par enquêteur
                                               String search,
                                               String type,
                                               Integer priorite,
                                               Boolean urgent,
                                               Pageable pageable);


    /**
     * Change l’état d’une demande d’enquête.
     *
     * @param demandeId  l’identifiant de la demande
     * @param nouvelEtat le nouvel état (enum ou String selon ton choix)
     * @return la demande mise à jour
     */
    EnqueteDTO changerEtatEnquete(Long demandeId, String nouvelEtat);

    EnqueteDTO updateProgression(Long enqueteId, int progression);

    EnqueteStatsDTO getStatsEtat(Long utilisateurId);

    EnqueteDTO assignerEnqueteur(Long enqueteId, EnqueteAssignationRequestDTO dto);

    EnqueteAllDTO findEnqueteByIdAll(Long enqueteId);
}
