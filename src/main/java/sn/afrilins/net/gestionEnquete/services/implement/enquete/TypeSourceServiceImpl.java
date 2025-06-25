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
import sn.afrilins.net.gestionEnquete.repository.enquete.TypeSourceRepository;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.TypeSourceDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.request.TypeSourceRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.TypeSourceService;
import sn.afrilins.net.gestionEnquete.services.mapper.enquete.TypeSourceMapper;
import sn.afrilins.net.gestionEnquete.util.ValidationUtils;

@AllArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PACKAGE)
@Transactional
public class TypeSourceServiceImpl implements TypeSourceService {

    private final TypeSourceRepository typeSourceRepository;

    private  final TypeSourceMapper typeSourceMapper;

    private final String ENTITY = "type_source";

    @Override
    public TypeSourceDTO createTypeSource(TypeSourceRequestDTO typeSource) {
        ValidationUtils.requireNonBlank(typeSource.getCode(), "code", ENTITY);
        ValidationUtils.requireNonBlank(typeSource.getLibelle(), "libelle", ENTITY);
        ValidationUtils.requireMinLength(typeSource.getCode(), 2, "code", ENTITY);
        ValidationUtils.requireMinLength(typeSource.getLibelle(), 2, "libelle", ENTITY);

        var typeSourceGetCode = typeSourceRepository.findFirstByCode(typeSource.getCode());
        if (typeSourceGetCode.isPresent()) {
            throw new CustomBadRequestException(
                    new BadRequestAlertException("type_source_code_existe", ENTITY, "code_existe"));
        }

        var typeSourceGetLibelle = typeSourceRepository.findFirstByLibelle(typeSource.getLibelle());
        if (typeSourceGetLibelle.isPresent()) {
            throw new CustomBadRequestException(
                    new BadRequestAlertException("type_source_libelle_existe", ENTITY, "libelle_existe"));
        }

        var entity = typeSourceMapper.toEntity(
                TypeSourceDTO.builder()
                        .code(typeSource.getCode())
                        .libelle(typeSource.getLibelle())
                        .build()
        );

        return typeSourceMapper.toDto(typeSourceRepository.save(entity));
    }

    @Override
    public TypeSourceDTO updateTypeSource(TypeSourceDTO typeSource) {
        ValidationUtils.requirePositiveId(typeSource.getId(), "id", ENTITY);
        ValidationUtils.requireNonBlank(typeSource.getCode(), "code", ENTITY);
        ValidationUtils.requireNonBlank(typeSource.getLibelle(), "libelle", ENTITY);
        ValidationUtils.requireMinLength(typeSource.getCode(), 2, "code", ENTITY);
        ValidationUtils.requireMinLength(typeSource.getLibelle(), 2, "libelle", ENTITY);

        var existing = typeSourceRepository.findById(typeSource.getId())
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("type_source_introuvable", ENTITY, "id_inexistant")));

        typeSourceRepository.findFirstByCode(typeSource.getCode())
                .filter(e -> !e.getId().equals(typeSource.getId()))
                .ifPresent(e -> {
                    throw new CustomBadRequestException(
                            new BadRequestAlertException("type_source_code_existe", ENTITY, "code_existe"));
                });

        typeSourceRepository.findFirstByLibelle(typeSource.getLibelle())
                .filter(e -> !e.getId().equals(typeSource.getId()))
                .ifPresent(e -> {
                    throw new CustomBadRequestException(
                            new BadRequestAlertException("type_source_libelle_existe", ENTITY, "libelle_existe"));
                });

        existing.setCode(typeSource.getCode());
        existing.setLibelle(typeSource.getLibelle());

        return typeSourceMapper.toDto(typeSourceRepository.save(existing));
    }

    @Override
    public void deleteTypeSource(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        var existing = typeSourceRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("type_source_introuvable", ENTITY, "id_inexistant")));

        typeSourceRepository.delete(existing);
    }

    @Override
    public TypeSourceDTO findTypeSourceById(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        return typeSourceRepository.findById(id)
                .map(typeSourceMapper::toDto)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("type_source_introuvable", ENTITY, "id_inexistant")));
    }

    @Override
    public Page<TypeSourceDTO> readAllTypeSources(Pageable pageable) {
        return typeSourceRepository.findAllTypeSource(pageable)
                .map(typeSourceMapper::toDto);
    }

}
