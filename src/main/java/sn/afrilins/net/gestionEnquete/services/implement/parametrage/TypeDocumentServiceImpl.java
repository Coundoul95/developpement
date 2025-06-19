package sn.afrilins.net.gestionEnquete.services.implement.parametrage;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.afrilins.net.gestionEnquete.domain.parametrage.TypeDocument;
import sn.afrilins.net.gestionEnquete.exception.BadRequestAlertException;
import sn.afrilins.net.gestionEnquete.exception.CustomBadRequestException;
import sn.afrilins.net.gestionEnquete.repository.parametrage.TypeDocumentRepository;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.TypeDocumentDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.TypeDocumentService;
import sn.afrilins.net.gestionEnquete.services.mapper.parametrage.TypeDocumentMapper;

import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PACKAGE)
@Transactional
public class TypeDocumentServiceImpl implements TypeDocumentService {

    private final TypeDocumentRepository typeDocumentRepository;

    private final TypeDocumentMapper typeDocumentMapper;


    /**
     * @param typeDocument
     * @return
     */
    @Override
    public TypeDocumentDTO createTypeDocument(TypeDocumentDTO typeDocument) {
        var tyepeDocumentGetCode = typeDocumentRepository.findFirstByCode(typeDocument.getCode());

        if (tyepeDocumentGetCode.isPresent()){
            throw new CustomBadRequestException(new BadRequestAlertException("type_document_code_existe", "type_document_code_existe", "type_document_code_existe"));
        }

        var tyepeDocumentGetLibelle = typeDocumentRepository.findFirstByLibelle(typeDocument.getLibelle());

        if (tyepeDocumentGetLibelle.isPresent()){
            throw new CustomBadRequestException(new BadRequestAlertException("type_document_libelle_existe", "type_document_libelle_existe", "type_document_libelle_existe"));
        }

        return typeDocumentMapper.toDto(typeDocumentRepository.save(typeDocumentMapper.toEntity(typeDocument)));
    }

    /**
     * @param dto
     * @return
     */
    @Override
    public TypeDocumentDTO updateTypeDocument(TypeDocumentDTO dto) {
        TypeDocument existing = typeDocumentRepository.findById(dto.getId())
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("type_document_absent", "type_document", "id.notfound")));

        if (dto.getCode() != null && !dto.getCode().isBlank()) {
            Optional<TypeDocument> tyepeDocumentGetCode = typeDocumentRepository.findFirstByCode(dto.getCode());
            if (tyepeDocumentGetCode.isPresent() && !tyepeDocumentGetCode.get().getId().equals(existing.getId())) {
                throw new CustomBadRequestException(
                        new BadRequestAlertException("type_document_code_existe", "type_document", "code.exists"));
            }
            existing.setCode(dto.getCode());
        }

        if (dto.getLibelle() != null && !dto.getLibelle().isBlank()) {
            Optional<TypeDocument> tyepeDocumentGetLibelle = typeDocumentRepository.findFirstByLibelle(dto.getLibelle());
            if (tyepeDocumentGetLibelle.isPresent() && !tyepeDocumentGetLibelle.get().getId().equals(existing.getId())) {
                throw new CustomBadRequestException(
                        new BadRequestAlertException("type_document_libelle_existe", "type_document", "libelle.exists"));
            }
            existing.setLibelle(dto.getLibelle());
        }

        TypeDocument updated = typeDocumentRepository.save(existing);
        return typeDocumentMapper.toDto(updated);
    }


    /**
     * @param id
     */
    @Override
    public void deleteTypeDocument(Long id) {
        if (!typeDocumentRepository.existsById(id)) {
            throw new CustomBadRequestException(new BadRequestAlertException("type_document_absent", "type_document", "id.notfound"));
        }
        typeDocumentRepository.deleteById(id);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public TypeDocumentDTO findTypeDocumentById(Long id) {
        var typeDocument = typeDocumentRepository.findById(id).orElseThrow(
                () -> new CustomBadRequestException(
                        new BadRequestAlertException("type_document_absent", "type_document", "id.notfound")
                )
        );
        return typeDocumentMapper.toDto(typeDocument);
    }

    /**
     * @param pageable
     * @return
     */
    @Override
    public Page<TypeDocumentDTO> readAllTypeDocument(Pageable pageable) {
        return typeDocumentRepository.findAllTypeDocument(pageable).map(typeDocumentMapper::toDto);
    }
}
