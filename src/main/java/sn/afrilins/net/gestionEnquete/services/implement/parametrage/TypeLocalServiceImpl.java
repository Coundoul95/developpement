package sn.afrilins.net.gestionEnquete.services.implement.parametrage;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.afrilins.net.gestionEnquete.exception.BadRequestAlertException;
import sn.afrilins.net.gestionEnquete.exception.CustomBadRequestException;
import sn.afrilins.net.gestionEnquete.repository.parametrage.TypeLocalRepository;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.TypeLocalDTO;
import sn.afrilins.net.gestionEnquete.services.mapper.parametrage.TypeLocalMapper;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.TypeLocalService;

import java.text.MessageFormat;

@AllArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PACKAGE)
@Transactional
public class TypeLocalServiceImpl implements TypeLocalService {
    private final TypeLocalRepository typeLocalRepository;

    static final String TYPE_LOCAL_NOT_FOUND_MESSAGE = "[TYPE LOCAL] La ressource n'existe pas {0}";

    static final String TYPE_LOCAL_NOT_FOUND_MESSAGE_LIBELLE = "[LIBELLE NIVEAU] existe deja";

    private final TypeLocalMapper typeLocalMapper;

    @Override
    public TypeLocalDTO createTypeLocal(TypeLocalDTO typeLocal) {

        if (typeLocal.getCode().isEmpty()){
            throw new CustomBadRequestException(new BadRequestAlertException("type_local_code_empty", "type_local_code_empty", "type_local_code_empty"));
        }

        var tyepeLocalgetCode = typeLocalRepository.findFirstByCode(typeLocal.getCode());

        if (tyepeLocalgetCode.isPresent()){
            throw new CustomBadRequestException(new BadRequestAlertException("type_local_code_existe", "type_local_code_existe", "type_local_code_existe"));
        }

        var tyepeLocalgetLibelle = typeLocalRepository.findFirstByLibelle(typeLocal.getLibelle());

        if (tyepeLocalgetLibelle.isPresent()){
            throw new CustomBadRequestException(new BadRequestAlertException("type_local_libelle_existe", "type_local_libelle_existe", "type_local_libelle_existe"));
        }

        return typeLocalMapper.toDto(typeLocalRepository.save(typeLocalMapper.toEntity(typeLocal)));
    }

    @Override
    public TypeLocalDTO updateTypeLocal(TypeLocalDTO typeLocal) {
        var tyepeLocalEntity = typeLocalRepository.findById(typeLocal.getId());

        if (tyepeLocalEntity.isEmpty()) {
            throw new CustomBadRequestException(new BadRequestAlertException("type_local_absent", "type_local_absent", "type_local_absent"));
//                    new ResourceNotFoundException(MessageFormat.format(TYPE_LOCAL_NOT_FOUND_MESSAGE, typeLocal.getId()));
        }

//        var tyepeLocalgetLibelle = typeLocalRepository.findFirstByLibelle(typeLocal.getLibelle());
//
//        if (tyepeLocalgetLibelle.isPresent()){
//            throw new CustomBadRequestException(new BadRequestAlertException("type_local_libelle_existe", "type_local_libelle_existe", "type_local_libelle_existe"));
//        }

        tyepeLocalEntity.get().setLibelle(typeLocal.getLibelle());

        return typeLocalMapper.toDto(typeLocalRepository.save(tyepeLocalEntity.get()));
    }

    @Override
    public void deleteTypeLocal(Long id) {
        if (!typeLocalRepository.existsById(id)) {
            throw new CustomBadRequestException(new BadRequestAlertException("type_local_absent", "type_local_absent", "type_local_absent"));
//                    new ResourceNotFoundException(MessageFormat.format(TYPE_LOCAL_NOT_FOUND_MESSAGE, id));
        }
        typeLocalRepository.deleteById(id);
    }

    @Override
    public TypeLocalDTO findTypeLocalById(Long id) {
        var tyepeLocal = typeLocalRepository.findById(id).orElseThrow(
                () -> new CustomBadRequestException(new BadRequestAlertException("type_local_absent", "type_local_absent", "type_local_absent")));
//                        new ResourceNotFoundException(MessageFormat.format(TYPE_LOCAL_NOT_FOUND_MESSAGE, id)));
        return typeLocalMapper.toDto(tyepeLocal);
    }

    @Override
    public Page<TypeLocalDTO> readAllTypeLocal(Pageable pageable, String code, String libelle) {
        var tyepeLocal = typeLocalRepository.findAllTypeLocal(pageable, code, libelle).map(typeLocalMapper::toDto);
        log.info("Type Local read all ====================== {}", tyepeLocal);
        return tyepeLocal;
    }
}
