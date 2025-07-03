package sn.afrilins.net.gestionEnquete.services.implement.enquete;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.afrilins.net.gestionEnquete.exception.BadRequestAlertException;
import sn.afrilins.net.gestionEnquete.exception.CustomBadRequestException;
import sn.afrilins.net.gestionEnquete.repository.enquete.EtatSourceInfoRepository;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.EtatSourceInfoDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.request.EtatSourceInfoRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.EtatSourcInfoService;
import sn.afrilins.net.gestionEnquete.services.mapper.enquete.EtatSourceInfoMapper;
import sn.afrilins.net.gestionEnquete.util.ValidationUtils;

@AllArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PACKAGE)
@Transactional
public class EtatSourceInfoServiceImpl implements EtatSourcInfoService {

    private final EtatSourceInfoRepository etatSourceInfoRepository;
    private  final EtatSourceInfoMapper etatSourceInfoMapper;
    private final String ENTITY = "etat_source_info";


    /**
     * {@inheritDoc}
     */
    @Override
    public EtatSourceInfoDTO createEtatSourceInfo(EtatSourceInfoRequestDTO etatSourceInfo) {
        ValidationUtils.requireNonBlank(etatSourceInfo.getCode(), "code", ENTITY);
        ValidationUtils.requireNonBlank(etatSourceInfo.getLibelle(), "libelle", ENTITY);
        ValidationUtils.requireMinLength(etatSourceInfo.getCode(), 2, "code", ENTITY);
        ValidationUtils.requireMinLength(etatSourceInfo.getLibelle(), 2, "libelle", ENTITY);

        var etatSourceInfoGetCode = this.etatSourceInfoRepository.findFirstByCode(etatSourceInfo.getCode());
        if (etatSourceInfoGetCode.isPresent()) {
            throw new CustomBadRequestException(
                    new BadRequestAlertException("etat_source_info_code_existe", ENTITY, "code_existe"));
        }

        var etatSourceInfoGetLibelle = etatSourceInfoRepository.findFirstByLibelle(etatSourceInfo.getLibelle());
        if (etatSourceInfoGetLibelle.isPresent()) {
            throw new CustomBadRequestException(
                    new BadRequestAlertException("etat_source_info_libelle_existe", ENTITY, "libelle_existe"));
        }

        var entity = etatSourceInfoMapper.toEntity(
                EtatSourceInfoDTO.builder()
                        .code(etatSourceInfo.getCode())
                        .libelle(etatSourceInfo.getLibelle())
                        .build()
        );

        return etatSourceInfoMapper.toDto(etatSourceInfoRepository.save(entity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EtatSourceInfoDTO updateEtatSourceInfo(EtatSourceInfoDTO etatSourceInfo) {
        ValidationUtils.requirePositiveId(etatSourceInfo.getId(), "id", ENTITY);

        var existing = etatSourceInfoRepository.findById(etatSourceInfo.getId())
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("etat_source_info_introuvable", ENTITY, "id_inexistant")));

        if(etatSourceInfo.getCode() != null){
            ValidationUtils.requireNonBlank(etatSourceInfo.getCode(), "code", ENTITY);
            ValidationUtils.requireMinLength(etatSourceInfo.getCode(), 2, "code", ENTITY);

            etatSourceInfoRepository.findFirstByCode(etatSourceInfo.getCode())
                    .filter(e -> !e.getId().equals(etatSourceInfo.getId()))
                    .ifPresent(e -> {
                        throw new CustomBadRequestException(
                                new BadRequestAlertException("etat_source_info_code_existe", ENTITY, "code_existe"));
                    });

            existing.setCode(etatSourceInfo.getCode());
        }

        if(etatSourceInfo.getLibelle() != null){
            ValidationUtils.requireNonBlank(etatSourceInfo.getLibelle(), "libelle", ENTITY);
            ValidationUtils.requireMinLength(etatSourceInfo.getLibelle(), 2, "libelle", ENTITY);

            etatSourceInfoRepository.findFirstByLibelle(etatSourceInfo.getLibelle())
                    .filter(e -> !e.getId().equals(etatSourceInfo.getId()))
                    .ifPresent(e -> {
                        throw new CustomBadRequestException(
                                new BadRequestAlertException("etat_source_info_libelle_existe", ENTITY, "libelle_existe"));
                    });

            existing.setLibelle(etatSourceInfo.getLibelle());
        }

        return etatSourceInfoMapper.toDto(etatSourceInfoRepository.save(existing));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEtatSourceInfo(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        var existing = etatSourceInfoRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("etat_source_info_introuvable", ENTITY, "id_inexistant")));

        etatSourceInfoRepository.delete(existing);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EtatSourceInfoDTO findEtatSourceInfoById(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        return etatSourceInfoRepository.findById(id)
                .map(etatSourceInfoMapper::toDto)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("etat_source_info_introuvable", ENTITY, "id_inexistant")));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<EtatSourceInfoDTO> readAllEtatSourceInfos(String code, String libelle, Pageable pageable) {
        return etatSourceInfoRepository
                .findAllEtatSourceInfo(code, libelle, pageable)
                .map(etatSourceInfoMapper::toDto);
    }
}
