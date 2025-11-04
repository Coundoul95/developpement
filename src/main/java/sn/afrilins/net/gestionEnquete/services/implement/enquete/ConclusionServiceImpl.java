package sn.afrilins.net.gestionEnquete.services.implement.enquete;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.afrilins.net.gestionEnquete.domain.enquete.Conclusion;
import sn.afrilins.net.gestionEnquete.exception.BadRequestAlertException;
import sn.afrilins.net.gestionEnquete.exception.CustomBadRequestException;
import sn.afrilins.net.gestionEnquete.repository.enquete.ConclusionRepository;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.conclusion.response.ConclusionDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.conclusion.request.ConclusionRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.ConclusionService;
import sn.afrilins.net.gestionEnquete.services.mapper.enquete.ConclusionMapper;
import sn.afrilins.net.gestionEnquete.util.ValidationUtils;

@AllArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PACKAGE)
@Transactional
public class ConclusionServiceImpl implements ConclusionService {

    final ConclusionRepository conclusionRepository;
    final ConclusionMapper conclusionMapper;

    final String ENTITY = "conclusion";

    @Override
    public ConclusionDTO createConclusion(ConclusionRequestDTO conclusion) {

        ValidationUtils.requireNonBlank(conclusion.getTitre(), "titre", ENTITY);
        ValidationUtils.requireMinLength(conclusion.getTitre(), 2, "titre", ENTITY);

        var existingTitre = conclusionRepository.findFirstByTitre(conclusion.getTitre());
        if (existingTitre.isPresent()) {
            throw new CustomBadRequestException(
                    new BadRequestAlertException("conclusion_titre_existe", ENTITY, "titre_existe"));
        }

        var entity = Conclusion.builder()
                .titre(conclusion.getTitre())
                .contenu(conclusion.getContenu())
                .recommandation(conclusion.getRecommandation())
                .mesuresSuivi(conclusion.getMesuresSuivi())
                .dateValidation(conclusion.getDateValidation())
                .build();

        return conclusionMapper.toDto(conclusionRepository.save(entity));
    }

    @Override
    public ConclusionDTO updateConclusion(ConclusionDTO conclusion) {

        ValidationUtils.requirePositiveId(conclusion.getId(), "id", ENTITY);
        ValidationUtils.requireNonBlank(conclusion.getTitre(), "titre", ENTITY);
        ValidationUtils.requireMinLength(conclusion.getTitre(), 2, "titre", ENTITY);

        var existing = conclusionRepository.findById(conclusion.getId())
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("conclusion_introuvable", ENTITY, "id_inexistant")));

        conclusionRepository.findFirstByTitre(conclusion.getTitre())
                .filter(e -> !e.getId().equals(conclusion.getId()))
                .ifPresent(e -> {
                    throw new CustomBadRequestException(
                            new BadRequestAlertException("conclusion_titre_existe", ENTITY, "titre_existe"));
                });

        existing.setTitre(conclusion.getTitre());
        existing.setContenu(conclusion.getContenu());
        existing.setRecommandation(conclusion.getRecommandation());
        existing.setMesuresSuivi(conclusion.getMesuresSuivi());
        existing.setDateValidation(conclusion.getDateValidation());

        return conclusionMapper.toDto(conclusionRepository.save(existing));
    }

    @Override
    public void deleteConclusion(Long id) {

        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        var existing = conclusionRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("conclusion_introuvable", ENTITY, "id_inexistant")));

        conclusionRepository.delete(existing);
    }

    @Override
    public ConclusionDTO findConclusionById(Long id) {

        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        return conclusionRepository.findById(id)
                .map(conclusionMapper::toDto)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("conclusion_introuvable", ENTITY, "id_inexistant")));
    }

    @Override
    public Page<ConclusionDTO> readAllConclusions(String titre, String contenu, String recommandation, String mesuresSuivi, Pageable pageable) {
        return conclusionRepository.findAllConclusion(titre, contenu, recommandation, mesuresSuivi, pageable)
                .map(conclusionMapper::toDto);
    }
}
