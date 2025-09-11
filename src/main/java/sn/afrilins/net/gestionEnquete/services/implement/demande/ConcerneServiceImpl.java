package sn.afrilins.net.gestionEnquete.services.implement.demande;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.afrilins.net.gestionEnquete.domain.demande.Concerne;
import sn.afrilins.net.gestionEnquete.domain.enume.TypeConcerne;
import sn.afrilins.net.gestionEnquete.exception.BadRequestAlertException;
import sn.afrilins.net.gestionEnquete.exception.CustomBadRequestException;
import sn.afrilins.net.gestionEnquete.repository.demande.ConcerneRepository;
import sn.afrilins.net.gestionEnquete.services.dto.demande.concerne.response.ConcerneDTO;
import sn.afrilins.net.gestionEnquete.services.dto.demande.concerne.request.ConcerneRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.demande.ConcerneService;
import sn.afrilins.net.gestionEnquete.services.mapper.demande.ConcerneMapper;
import sn.afrilins.net.gestionEnquete.util.ValidationUtils;


@AllArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PACKAGE)
@Transactional
public class ConcerneServiceImpl implements ConcerneService {

    final ConcerneRepository concerneRepository;
    final ConcerneMapper concerneMapper;

    final String ENTITY = "concerne";

    @Override
    public ConcerneDTO createConcerne(ConcerneRequestDTO dto) {
        log.info("Creating new Concerne: {}", dto);
        ValidationUtils.requireNonNull(dto.getType(), "type", ENTITY);
        ValidationUtils.requireNonBlank(dto.getTelephone(), "numero", ENTITY);
        ValidationUtils.requireNonBlank(dto.getRegionSocial(), "regionSocial", ENTITY);

        concerneRepository.findByTelephone(dto.getTelephone()).ifPresent(c -> {
            throw new CustomBadRequestException(
                    new BadRequestAlertException("numero_existe", ENTITY, "numero"));
        });

        var entity = Concerne.builder()
                .type(dto.getType()) // convert enum to string
                .telephone(dto.getTelephone())
                .regionSocial(dto.getRegionSocial())
                .build();

        return concerneMapper.toDto(concerneRepository.save(entity));
    }

    @Override
    public ConcerneDTO updateConcerne(ConcerneDTO dto) {

        ValidationUtils.requirePositiveId(dto.getId(), "id", ENTITY);
        ValidationUtils.requireNonNull(dto.getType(), "type", ENTITY);
        ValidationUtils.requireNonBlank(dto.getTelephone(), "numero", ENTITY);
        ValidationUtils.requireNonBlank(dto.getRegionSocial(), "regionSocial", ENTITY);

        var existing = concerneRepository.findById(dto.getId())
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("concerne_introuvable", ENTITY, "id_inexistant")));

        concerneRepository.findByTelephone(dto.getTelephone())
                .filter(c -> !c.getId().equals(dto.getId()))
                .ifPresent(c -> {
                    throw new CustomBadRequestException(
                            new BadRequestAlertException("numero_existe", ENTITY, "numero"));
                });

        existing.setType(dto.getType());
        existing.setTelephone(dto.getTelephone());
        existing.setRegionSocial(dto.getRegionSocial());

        return concerneMapper.toDto(concerneRepository.save(existing));
    }

    @Override
    public void deleteConcerne(Long id) {

        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        var concerne = concerneRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("concerne_introuvable", ENTITY, "id_inexistant")));

        concerneRepository.delete(concerne);
    }

    @Override
    public ConcerneDTO findConcerneById(Long id) {

        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        return concerneRepository.findById(id)
                .map(concerneMapper::toDto)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("concerne_introuvable", ENTITY, "id_inexistant")));
    }

    @Override
    public Page<ConcerneDTO> readAllConcernes(TypeConcerne type, String telephone, String regionSocial, Pageable pageable) {
        return concerneRepository.findAllConcerne(type, telephone, regionSocial, pageable)
                .map(concerneMapper::toDto);
    }
}
