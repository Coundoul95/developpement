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
import sn.afrilins.net.gestionEnquete.exception.BadRequestAlertException;
import sn.afrilins.net.gestionEnquete.exception.CustomBadRequestException;
import sn.afrilins.net.gestionEnquete.repository.enquete.AutreInfoRepository;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.autre_info.response.AutreInfoDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.autre_info.request.AutreInfoRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.AutreInfoService;
import sn.afrilins.net.gestionEnquete.services.mapper.enquete.AutreInfoMapper;
import sn.afrilins.net.gestionEnquete.util.ValidationUtils;

@AllArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PACKAGE)
@Transactional
public class AutreInfoServiceImpl implements AutreInfoService {

    final AutreInfoRepository autreInfoRepository;
    final AutreInfoMapper autreInfoMapper;

    final String ENTITY = "autre_info";

    @Override
    public AutreInfoDTO createAutreInfo(AutreInfoRequestDTO autreInfo) {

        ValidationUtils.requireNonBlank(autreInfo.getCategorie(), "categorie", ENTITY);
        ValidationUtils.requireNonBlank(autreInfo.getImportance(), "importance", ENTITY);
        ValidationUtils.requireMinLength(autreInfo.getCategorie(), 2, "categorie", ENTITY);

        var existingCategorie = autreInfoRepository.findFirstByCategorie(autreInfo.getCategorie());
        if (existingCategorie.isPresent()) {
            throw new CustomBadRequestException(
                    new BadRequestAlertException("autre_info_categorie_existe", ENTITY, "categorie_existe"));
        }

        var entity = AutreInfo.builder()
                .categorie(autreInfo.getCategorie())
                .description(autreInfo.getDescription())
                .importance(autreInfo.getImportance())
                .build();

        return autreInfoMapper.toDto(autreInfoRepository.save(entity));
    }

    @Override
    public AutreInfoDTO updateAutreInfo(AutreInfoDTO autreInfo) {

        ValidationUtils.requirePositiveId(autreInfo.getId(), "id", ENTITY);
        ValidationUtils.requireNonBlank(autreInfo.getCategorie(), "categorie", ENTITY);
        ValidationUtils.requireNonBlank(autreInfo.getImportance(), "importance", ENTITY);
        ValidationUtils.requireMinLength(autreInfo.getCategorie(), 2, "categorie", ENTITY);

        var existing = autreInfoRepository.findById(autreInfo.getId())
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("autre_info_introuvable", ENTITY, "id_inexistant")));

        autreInfoRepository.findFirstByCategorie(autreInfo.getCategorie())
                .filter(e -> !e.getId().equals(autreInfo.getId()))
                .ifPresent(e -> {
                    throw new CustomBadRequestException(
                            new BadRequestAlertException("autre_info_categorie_existe", ENTITY, "categorie_existe"));
                });

        existing.setCategorie(autreInfo.getCategorie());
        existing.setDescription(autreInfo.getDescription());
        existing.setImportance(autreInfo.getImportance());

        return autreInfoMapper.toDto(autreInfoRepository.save(existing));
    }

    @Override
    public void deleteAutreInfo(Long id) {

        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        var existing = autreInfoRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("autre_info_introuvable", ENTITY, "id_inexistant")));

        autreInfoRepository.delete(existing);
    }

    @Override
    public AutreInfoDTO findAutreInfoById(Long id) {

        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        return autreInfoRepository.findById(id)
                .map(autreInfoMapper::toDto)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("autre_info_introuvable", ENTITY, "id_inexistant")));
    }

    @Override
    public Page<AutreInfoDTO> readAllAutreInfos(String categorie, String description, String importance, Pageable pageable) {
        return autreInfoRepository.findAllAutreInfo(categorie, description, importance, pageable)
                .map(autreInfoMapper::toDto);
    }
}
