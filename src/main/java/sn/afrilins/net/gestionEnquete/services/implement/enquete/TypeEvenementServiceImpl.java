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
import sn.afrilins.net.gestionEnquete.repository.enquete.TypeEvenementRepository;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.calendrier.response.TypeEvenementDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.calendrier.request.TypeEvenementRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.TypeEvenementService;
import sn.afrilins.net.gestionEnquete.services.mapper.enquete.TypeEvenementMapper;
import sn.afrilins.net.gestionEnquete.util.ValidationUtils;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
@FieldDefaults(level = AccessLevel.PACKAGE)
public class TypeEvenementServiceImpl implements TypeEvenementService {

    final TypeEvenementRepository repository;
    final TypeEvenementMapper mapper;

    final String ENTITY = "type_evenement";

    @Override
    public TypeEvenementDTO createTypeEvenement(TypeEvenementRequestDTO dto) {
        ValidationUtils.requireNonBlank(dto.getCode(), "code", ENTITY);
        ValidationUtils.requireNonBlank(dto.getLibelle(), "libelle", ENTITY);
        ValidationUtils.requireMinLength(dto.getCode(), 2, "code", ENTITY);
        ValidationUtils.requireMinLength(dto.getLibelle(), 2, "libelle", ENTITY);

        repository.findFirstByCode(dto.getCode()).ifPresent(e -> {
            throw new CustomBadRequestException(
                    new BadRequestAlertException("type_evenement_code_existe", ENTITY, "code_existe")
            );
        });

        repository.findFirstByLibelle(dto.getLibelle()).ifPresent(e -> {
            throw new CustomBadRequestException(
                    new BadRequestAlertException("type_evenement_libelle_existe", ENTITY, "libelle_existe")
            );
        });

        var entity = mapper.toEntity(
                TypeEvenementDTO.builder()
                        .code(dto.getCode())
                        .libelle(dto.getLibelle())
                        .build()
        );

        return mapper.toDto(repository.save(entity));
    }

    @Override
    public TypeEvenementDTO updateTypeEvenement(TypeEvenementDTO dto) {
        ValidationUtils.requirePositiveId(dto.getId(), "id", ENTITY);
        ValidationUtils.requireNonBlank(dto.getCode(), "code", ENTITY);
        ValidationUtils.requireNonBlank(dto.getLibelle(), "libelle", ENTITY);
        ValidationUtils.requireMinLength(dto.getCode(), 2, "code", ENTITY);
        ValidationUtils.requireMinLength(dto.getLibelle(), 2, "libelle", ENTITY);

        var existing = repository.findById(dto.getId())
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("type_evenement_introuvable", ENTITY, "id_inexistant")
                ));

        repository.findFirstByCode(dto.getCode())
                .filter(e -> !e.getId().equals(dto.getId()))
                .ifPresent(e -> {
                    throw new CustomBadRequestException(
                            new BadRequestAlertException("type_evenement_code_existe", ENTITY, "code_existe")
                    );
                });

        repository.findFirstByLibelle(dto.getLibelle())
                .filter(e -> !e.getId().equals(dto.getId()))
                .ifPresent(e -> {
                    throw new CustomBadRequestException(
                            new BadRequestAlertException("type_evenement_libelle_existe", ENTITY, "libelle_existe")
                    );
                });

        existing.setCode(dto.getCode());
        existing.setLibelle(dto.getLibelle());

        return mapper.toDto(repository.save(existing));
    }

    @Override
    public void deleteTypeEvenement(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        var existing = repository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("type_evenement_introuvable", ENTITY, "id_inexistant")
                ));

        repository.delete(existing);
    }

    @Override
    public TypeEvenementDTO findTypeEvenementById(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("type_evenement_introuvable", ENTITY, "id_inexistant")
                ));
    }

    @Override
    public Page<TypeEvenementDTO> readAllTypeEvenements(String code, String libelle, Pageable pageable) {
        return repository.findAllTypeEvenement(code, libelle, pageable)
                .map(mapper::toDto);
    }
}
