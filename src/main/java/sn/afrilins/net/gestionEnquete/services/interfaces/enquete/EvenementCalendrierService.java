package sn.afrilins.net.gestionEnquete.services.interfaces.enquete;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.EvenementCalendrierDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.StatistiqueCalendrierDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.request.EvenementCalendrierRequestDTO;

import java.time.LocalDate;

public interface EvenementCalendrierService {


    /**
     * Crée un nouvel événement calendrier.
     *
     * @param evenementRequest données du nouvel événement
     * @return l'événement créé (DTO)
     */
    EvenementCalendrierDTO createEvenement(EvenementCalendrierRequestDTO evenementRequest);

    /**
     * Met à jour un événement calendrier existant.
     *
     * @param evenementDTO données mises à jour de l'événement
     * @return l'événement mis à jour (DTO)
     */
    EvenementCalendrierDTO updateEvenement(Long id, EvenementCalendrierRequestDTO evenementDTO);

    /**
     * Supprime un événement calendrier par son identifiant.
     *
     * @param id identifiant de l'événement à supprimer
     */
    void deleteEvenement(Long id);

    /**
     * Recherche un événement calendrier par son identifiant.
     *
     * @param id identifiant de l'événement
     * @return l'événement trouvé (DTO)
     */
    EvenementCalendrierDTO findEvenementById(Long id);

    /**
     * Recherche paginée d'événements calendrier avec filtres dynamiques.
     *
     * @param search       recherche textuelle globale
     * @param titre        filtre par titre (partiel)
     * @param heure        filtre par heure exacte
     * @param duree        filtre par durée
     * @param priorite     filtre par priorité
     * @param date         filtre par date exacte
     * @param utilisateurId filtre par identifiant utilisateur
     * @param typeCode     filtre par code type d'événement
     * @param pageable     pagination et tri
     * @return page des événements correspondant aux critères
     */
    Page<EvenementCalendrierDTO> readAllEvenements(
            String search,
            String titre,
            String heure,
            Integer duree,
            String priorite,
            LocalDate date,
            Long utilisateurId,
            String typeCode,
            Pageable pageable
    );

    StatistiqueCalendrierDTO getStatistiquesSemaine(Long utilisateurId);
}
