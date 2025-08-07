package sn.afrilins.net.gestionEnquete.services.implement.parametrage;

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
import sn.afrilins.net.gestionEnquete.repository.parametrage.TypeDocumentRepository;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.TypeDocumentDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.request.TypeDocumentRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.TypeDocumentService;
import sn.afrilins.net.gestionEnquete.services.mapper.parametrage.TypeDocumentMapper;
import sn.afrilins.net.gestionEnquete.util.ValidationUtils;

@AllArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PACKAGE)
@Transactional
public class TypeDocumentServiceImpl implements TypeDocumentService {

    final TypeDocumentRepository typeDocumentRepository;
    final TypeDocumentMapper typeDocumentMapper;
    final String ENTITY = "type_document";

    @Override
    public TypeDocumentDTO createTypeDocument(TypeDocumentRequestDTO request) {
        ValidationUtils.requireNonBlank(request.getCode(), "code", ENTITY);
        ValidationUtils.requireNonBlank(request.getLibelle(), "libelle", ENTITY);
        ValidationUtils.requireMinLength(request.getCode(), 2, "code", ENTITY);
        ValidationUtils.requireMinLength(request.getLibelle(), 2, "libelle", ENTITY);

        typeDocumentRepository.findFirstByCode(request.getCode()).ifPresent(e -> {
            throw new CustomBadRequestException(
                    new BadRequestAlertException("type_document_code_existe", ENTITY, "code_existe"));
        });

        typeDocumentRepository.findFirstByLibelle(request.getLibelle()).ifPresent(e -> {
            throw new CustomBadRequestException(
                    new BadRequestAlertException("type_document_libelle_existe", ENTITY, "libelle_existe"));
        });

        var entity = typeDocumentMapper.toEntity(
                TypeDocumentDTO.builder()
                        .code(request.getCode())
                        .libelle(request.getLibelle())
                        .build()
        );

        return typeDocumentMapper.toDto(typeDocumentRepository.save(entity));
    }

    @Override
    public TypeDocumentDTO updateTypeDocument(TypeDocumentDTO dto) {
        ValidationUtils.requirePositiveId(dto.getId(), "id", ENTITY);
        ValidationUtils.requireNonBlank(dto.getCode(), "code", ENTITY);
        ValidationUtils.requireNonBlank(dto.getLibelle(), "libelle", ENTITY);
        ValidationUtils.requireMinLength(dto.getCode(), 2, "code", ENTITY);
        ValidationUtils.requireMinLength(dto.getLibelle(), 2, "libelle", ENTITY);

        var existing = typeDocumentRepository.findById(dto.getId())
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("type_document_introuvable", ENTITY, "id_inexistant")));

        typeDocumentRepository.findFirstByCode(dto.getCode())
                .filter(e -> !e.getId().equals(dto.getId()))
                .ifPresent(e -> {
                    throw new CustomBadRequestException(
                            new BadRequestAlertException("type_document_code_existe", ENTITY, "code_existe"));
                });

        typeDocumentRepository.findFirstByLibelle(dto.getLibelle())
                .filter(e -> !e.getId().equals(dto.getId()))
                .ifPresent(e -> {
                    throw new CustomBadRequestException(
                            new BadRequestAlertException("type_document_libelle_existe", ENTITY, "libelle_existe"));
                });

        existing.setCode(dto.getCode());
        existing.setLibelle(dto.getLibelle());

        return typeDocumentMapper.toDto(typeDocumentRepository.save(existing));
    }

    @Override
    public void deleteTypeDocument(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        var existing = typeDocumentRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("type_document_introuvable", ENTITY, "id_inexistant")));

        typeDocumentRepository.delete(existing);
    }

    @Override
    public TypeDocumentDTO findTypeDocumentById(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        return typeDocumentRepository.findById(id)
                .map(typeDocumentMapper::toDto)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("type_document_introuvable", ENTITY, "id_inexistant")));
    }

    @Override
    public TypeDocumentDTO findTypeDocumentByCode(String code) {
        ValidationUtils.requireNonBlank(code, "code", ENTITY);
        ValidationUtils.requireMinLength(code, 2,"code", ENTITY);
        return typeDocumentRepository.findFirstByCode(code)
                .map(typeDocumentMapper::toDto)
                .orElseThrow( () ->new CustomBadRequestException(
                        new BadRequestAlertException("type_document_introuvable", ENTITY, "code_inexistant")));
    }

    @Override
    public Page<TypeDocumentDTO> readAllTypeDocument(String code, String libelle, Pageable pageable) {
        return typeDocumentRepository.findAllTypeDocument(code, libelle, pageable)
                .map(typeDocumentMapper::toDto);
    }
}
