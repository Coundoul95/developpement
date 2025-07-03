package sn.afrilins.net.gestionEnquete.services.interfaces.enquete;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.SourceInfoDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.request.SourceInfoRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.request.SourceInfoUpdateRequestDTO;

import javax.validation.Valid;
import java.util.List;

public interface SourceInfoService {

    /**
     * Crée un nouvel source d'information.
     *
     * @param sourceInfo les données de la source à créer
     * @return la source d'information créé
     */
    SourceInfoDTO createSourceInfo(SourceInfoRequestDTO sourceInfo);

    /**
     * Met à jour un source d'information existant.
     *
     * @param sourceInfo les nouvelles données de la source d'information
     * @return la source d'information mis à jour
     */
    SourceInfoDTO updateSourceInfo(SourceInfoUpdateRequestDTO sourceInfo);

    /**
     * Supprime une source d'information par son identifiant.
     *
     * @param id l'identifiant de la source à supprimer
     */
    void deleteSourceInfo(Long id);

    /**
     * Recherche une source d'information par son identifiant.
     *
     * @param id l'identifiant de la source d'information
     * @return la source trouvé
     */
    SourceInfoDTO findSourceInfoById(Long id);

    Page<SourceInfoDTO> readAllSourceInfo(Long utilisateurId, String nom, String niveauFiabilite, Long etatId, String search, Pageable pageable);

    SourceInfoDTO createSourceInfoWithFiles(@Valid SourceInfoRequestDTO requestDTO, List<MultipartFile> files);
}
