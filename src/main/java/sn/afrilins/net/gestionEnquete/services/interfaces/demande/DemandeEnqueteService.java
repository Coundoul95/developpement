package sn.afrilins.net.gestionEnquete.services.interfaces.demande;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import sn.afrilins.net.gestionEnquete.services.dto.demande.demande_enquete.response.*;
import sn.afrilins.net.gestionEnquete.services.dto.demande.demande_enquete.request.DemandeEnqueteRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.demande.demande_enquete.request.DemandeEnqueteUpdateRequestDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface pour la gestion des opérations liées aux demandes d’enquête.
 */
public interface

DemandeEnqueteService {

    /**
     * Crée une nouvelle demande d’enquête.
     *
     * @param dto les données de la demande
     * @return la demande créée
     */
    DemandeEnqueteDTO createDemandeEnquete(DemandeEnqueteRequestDTO dto);

    /**
     * Met à jour une demande d’enquête existante.
     *
     * @param dto les nouvelles données
     * @return la demande mise à jour
     */
    DemandeEnqueteDTO updateDemandeEnquete(Long id, DemandeEnqueteUpdateRequestDTO dto);

    /**
     * Supprime une demande d’enquête.
     *
     * @param id l’identifiant de la demande
     */
    void deleteDemandeEnquete(Long id);

    /**
     * Récupère une demande d’enquête par son identifiant.
     *
     * @param id l’identifiant de la demande
     * @return la demande trouvée
     */
    DemandeEnqueteDTO findDemandeEnqueteById(Long id);

    /**
     * Liste paginée des demandes d’enquête.
     *
     * @param pageable les paramètres de pagination
     * @return une page de demandes
     */
    Page<DemandeEnqueteDTO> findAllDemandeEnquete(Long utilisateurId, Boolean urgent,String objet, String type, String etat,String etatEnquete, Integer priorite, String search, Pageable pageable);

    /**
     * Change l’état d’une demande d’enquête.
     *
     * @param demandeId l’identifiant de la demande
     * @param nouvelEtat le nouvel état (enum ou String selon ton choix)
     * @return la demande mise à jour
     */
    DemandeEnqueteDTO changerEtatDemandeEnquete(Long demandeId, String nouvelEtat);

    DemandeEnqueteDTO createDemandeEnqueteAvecDocuments(DemandeEnqueteRequestDTO dto, MultipartFile[] files);

    DemandeEnqueteStatsDTO getStatsEtat(Long utilisateurId);

    DemandeValideEnqueteStatsDTO getStatsEtatEnquete(Long utilisateurId);

    DemandeStatistiquesDTO getStatistiques(Long utilisateurId);

    List<DemandeSerieDTO> getEvolutionDemandes(Long utilisateurId, LocalDate date);

}
