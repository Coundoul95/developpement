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
import sn.afrilins.net.gestionEnquete.repository.enquete.EtatEnqueteRepository;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.EtatEnqueteDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.request.EtatEnqueteRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.EtatEnqueteService;
import sn.afrilins.net.gestionEnquete.services.mapper.enquete.EtatEnqueteMapper;
import sn.afrilins.net.gestionEnquete.util.ValidationUtils;

@AllArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PACKAGE)
@Transactional
public class EtatEnqueteServiceImpl implements EtatEnqueteService {

    private final EtatEnqueteRepository etatEnqueteRepository;

    private  final EtatEnqueteMapper etatEnqueteMapper;

    private final String ENTITY = "etat_enquete";
    
    
    @Override
    public EtatEnqueteDTO createEtatEnquete(EtatEnqueteRequestDTO etatEnquete) {
        ValidationUtils.requireNonBlank(etatEnquete.getCode(), "code", ENTITY);
        ValidationUtils.requireNonBlank(etatEnquete.getLibelle(), "libelle", ENTITY);
        ValidationUtils.requireMinLength(etatEnquete.getCode(), 2, "code", ENTITY);
        ValidationUtils.requireMinLength(etatEnquete.getLibelle(), 2, "libelle", ENTITY);

        var etatEnqueteGetCode = etatEnqueteRepository.findFirstByCode(etatEnquete.getCode());
        if (etatEnqueteGetCode.isPresent()) {
            throw new CustomBadRequestException(
                    new BadRequestAlertException("etat_enquete_code_existe", ENTITY, "code_existe"));
        }

        var etatEnqueteGetLibelle = etatEnqueteRepository.findFirstByLibelle(etatEnquete.getLibelle());
        if (etatEnqueteGetLibelle.isPresent()) {
            throw new CustomBadRequestException(
                    new BadRequestAlertException("etat_enquete_libelle_existe", ENTITY, "libelle_existe"));
        }

        var entity = etatEnqueteMapper.toEntity(
                EtatEnqueteDTO.builder()
                        .code(etatEnquete.getCode())
                        .libelle(etatEnquete.getLibelle())
                        .build()
        );

        return etatEnqueteMapper.toDto(etatEnqueteRepository.save(entity));
    }

    @Override
    public EtatEnqueteDTO updateEtatEnquete(EtatEnqueteDTO etatEnquete) {
        ValidationUtils.requirePositiveId(etatEnquete.getId(), "id", ENTITY);
        ValidationUtils.requireNonBlank(etatEnquete.getCode(), "code", ENTITY);
        ValidationUtils.requireNonBlank(etatEnquete.getLibelle(), "libelle", ENTITY);
        ValidationUtils.requireMinLength(etatEnquete.getCode(), 2, "code", ENTITY);
        ValidationUtils.requireMinLength(etatEnquete.getLibelle(), 2, "libelle", ENTITY);

        var existing = etatEnqueteRepository.findById(etatEnquete.getId())
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("etat_enquete_introuvable", ENTITY, "id_inexistant")));

        etatEnqueteRepository.findFirstByCode(etatEnquete.getCode())
                .filter(e -> !e.getId().equals(etatEnquete.getId()))
                .ifPresent(e -> {
                    throw new CustomBadRequestException(
                            new BadRequestAlertException("etat_enquete_code_existe", ENTITY, "code_existe"));
                });

        etatEnqueteRepository.findFirstByLibelle(etatEnquete.getLibelle())
                .filter(e -> !e.getId().equals(etatEnquete.getId()))
                .ifPresent(e -> {
                    throw new CustomBadRequestException(
                            new BadRequestAlertException("etat_enquete_libelle_existe", ENTITY, "libelle_existe"));
                });

        existing.setCode(etatEnquete.getCode());
        existing.setLibelle(etatEnquete.getLibelle());

        return etatEnqueteMapper.toDto(etatEnqueteRepository.save(existing));
    }

    @Override
    public void deleteEtatEnquete(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        var existing = etatEnqueteRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("etat_enquete_introuvable", ENTITY, "id_inexistant")));

        etatEnqueteRepository.delete(existing);
    }

    @Override
    public EtatEnqueteDTO findEtatEnqueteById(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        return etatEnqueteRepository.findById(id)
                .map(etatEnqueteMapper::toDto)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("etat_enquete_introuvable", ENTITY, "id_inexistant")));

    }

    @Override
    public Page<EtatEnqueteDTO> readAllEtatEnquetes(Pageable pageable) {
        return etatEnqueteRepository.findAllEtatEnquete(pageable)
                .map(etatEnqueteMapper::toDto);
    }
}
