package sn.afrilins.net.gestionEnquete.services.implement.enquete;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.afrilins.net.gestionEnquete.domain.enquete.AutreInfo;
import sn.afrilins.net.gestionEnquete.domain.enquete.EtatAutreInfo;
import sn.afrilins.net.gestionEnquete.exception.BadRequestAlertException;
import sn.afrilins.net.gestionEnquete.exception.CustomBadRequestException;
import sn.afrilins.net.gestionEnquete.repository.enquete.AutreInfoRepository;
import sn.afrilins.net.gestionEnquete.repository.enquete.EtatAutreInfoRepository;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.autre_info.request.AutreInfoRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.autre_info.request.AutreInfoUpdateRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.autre_info.response.AutreInfoDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.AutreInfoService;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.EnqueteService;
import sn.afrilins.net.gestionEnquete.services.mapper.enquete.AutreInfoMapper;
import sn.afrilins.net.gestionEnquete.util.ValidationUtils;

import java.time.LocalDateTime;

@AllArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class AutreInfoServiceImpl implements AutreInfoService {

    AutreInfoRepository autreInfoRepository;
    AutreInfoMapper autreInfoMapper;
    EnqueteService enqueteService;
    EtatAutreInfoRepository etatAutreInfoRepository;

    static final String ENTITY = "autre_info";

    // ------------------------
    // CREATE
    // ------------------------
    @Override
    public AutreInfoDTO createAutreInfo(AutreInfoRequestDTO request) {
        log.debug("Création d'une nouvelle AutreInfo : {}", request);

        // Valeurs par défaut
        if (request.getImportance() == null) {
            request.setImportance(3); // importance par défaut
        }
        if (request.getDateEnregistrement() == null) {
            request.setDateEnregistrement(LocalDateTime.now());
        }

        // Validations
        validateRequest(request);

        // Vérifier existence de l'enquête
        var enquete = enqueteService.getEnqueteOrThrow(request.getEnqueteId());

        // Vérifier existence de l'état
        var etat = getEtatOrThrow(request.getCodeEtat());

        // Mapper en entité
        var entity = AutreInfo.builder()
                .categorie(request.getCategorie())
                .objet(request.getObjet())
                .description(request.getDescription())
                .importance(request.getImportance())
                .enquete(enquete)
                .etat(etat)
                .dateEnregistrement(request.getDateEnregistrement())
                .build();

        return autreInfoMapper.toDto(autreInfoRepository.save(entity));
    }

    // ------------------------
    // UPDATE
    // ------------------------
    @Override
    public AutreInfoDTO updateAutreInfo(Long id, AutreInfoUpdateRequestDTO dto) {
        log.debug("Mise à jour de AutreInfo id={} : {}", id, dto);

        // Validation de l'ID
        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        // Récupération de l'entité existante
        var existing = autreInfoRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("autre_info_introuvable", ENTITY, "id_inexistant")));

        // Mise à jour conditionnelle avec validations
        dto.getCategorie().ifPresent(categorie -> {
            ValidationUtils.requireNonBlank(categorie, "categorie", ENTITY);
            ValidationUtils.requireMinLength(categorie, 2, "categorie", ENTITY);
            existing.setCategorie(categorie);
        });

        dto.getObjet().ifPresent(existing::setObjet);

        dto.getDescription().ifPresent(existing::setDescription);

        dto.getImportance().ifPresent(importance -> {
            ValidationUtils.requireInRange(importance, 1, 5, "importance", ENTITY);
            existing.setImportance(importance);
        });

        dto.getCodeEtat().ifPresent(codeEtat -> {
            var etat = getEtatOrThrow(codeEtat);
            existing.setEtat(etat);
        });

        dto.getDateEnregistrement().ifPresent(existing::setDateEnregistrement);

        // Sauvegarde
        return autreInfoMapper.toDto(autreInfoRepository.save(existing));
    }

    // ------------------------
    // DELETE
    // ------------------------
    @Override
    public void deleteAutreInfo(Long id) {
        log.debug("Suppression de AutreInfo id={}", id);

        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        var existing = autreInfoRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("autre_info_introuvable", ENTITY, "id_inexistant")));

        autreInfoRepository.delete(existing);
    }

    // ------------------------
    // READ
    // ------------------------
    @Override
    @Transactional(readOnly = true)
    public AutreInfoDTO findAutreInfoById(Long id) {
        log.debug("Recherche AutreInfo par id={}", id);

        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        return autreInfoRepository.findById(id)
                .map(autreInfoMapper::toDto)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("autre_info_introuvable", ENTITY, "id_inexistant")));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AutreInfoDTO> readAllAutreInfos(
            String categorie,
            String objet,
            String description,
            Integer importance,
            String codeEtat,
            String search,
            Pageable pageable) {

        log.debug("Recherche paginée des AutreInfos [categorie={}, objet={}, importance={}, description={}, codeEtat={}]",
                categorie, objet, importance, description, codeEtat);

        return autreInfoRepository.findAllAutreInfo(
                        categorie, objet, description, importance, codeEtat, search, pageable)
                .map(autreInfoMapper::toDto);
    }

    // ------------------------
    // UTILS
    // ------------------------
    private EtatAutreInfo getEtatOrThrow(String code) {
        return etatAutreInfoRepository.findFirstByCode(code)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("etat_introuvable", ENTITY, "etat_inexistant")));
    }

    private void validateRequest(AutreInfoRequestDTO request) {
        ValidationUtils.requireNonBlank(request.getCategorie(), "categorie", ENTITY);
        ValidationUtils.requireMinLength(request.getCategorie(), 2, "categorie", ENTITY);
        ValidationUtils.requireInRange(request.getImportance(), 1, 5, "importance", ENTITY);
        ValidationUtils.requirePositiveId(request.getEnqueteId(), "enqueteId", ENTITY);
        ValidationUtils.requireNonBlank(request.getObjet(), "objet", ENTITY);
        ValidationUtils.requireNonBlank(request.getCodeEtat(), "codeEtat", ENTITY);
    }
}
