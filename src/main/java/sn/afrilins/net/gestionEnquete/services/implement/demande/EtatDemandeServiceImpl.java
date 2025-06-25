package sn.afrilins.net.gestionEnquete.services.implement.demande;

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
import sn.afrilins.net.gestionEnquete.repository.demande.EtatDemandeRepository;
import sn.afrilins.net.gestionEnquete.services.dto.demande.EtatDemandeDTO;
import sn.afrilins.net.gestionEnquete.services.dto.demande.request.EtatDemandeRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.demande.EtatDemandeService;
import sn.afrilins.net.gestionEnquete.services.mapper.demande.EtatDemandeMapper;
import sn.afrilins.net.gestionEnquete.util.ValidationUtils;


@AllArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PACKAGE)
@Transactional
public class EtatDemandeServiceImpl implements EtatDemandeService {

    private  final EtatDemandeRepository etatDemandeRepository;

    private final EtatDemandeMapper etatDemandeMapper;

    private final String ENTITY = "etat_demande";


    /**
     * {@inheritDoc}
     */
    @Override
    public EtatDemandeDTO createEtatDemande(EtatDemandeRequestDTO etatDemande) {
        ValidationUtils.requireNonBlank(etatDemande.getCode(), "code", ENTITY);
        ValidationUtils.requireNonBlank(etatDemande.getLibelle(), "libelle", ENTITY);
        ValidationUtils.requireMinLength(etatDemande.getCode(), 2, "code", ENTITY);
        ValidationUtils.requireMinLength(etatDemande.getLibelle(), 2, "libelle", ENTITY);

        var etatDemandeGetCode = etatDemandeRepository.findFirstByCode(etatDemande.getCode());
        if (etatDemandeGetCode.isPresent()) {
            throw new CustomBadRequestException(
                    new BadRequestAlertException("etat_demande_code_existe", ENTITY, "code_existe"));
        }

        var etatDemandeGetLibelle = etatDemandeRepository.findFirstByLibelle(etatDemande.getLibelle());
        if (etatDemandeGetLibelle.isPresent()) {
            throw new CustomBadRequestException(
                    new BadRequestAlertException("etat_demande_libelle_existe", ENTITY, "libelle_existe"));
        }

        var entity = etatDemandeMapper.toEntity(
                EtatDemandeDTO.builder()
                        .code(etatDemande.getCode())
                        .libelle(etatDemande.getLibelle())
                        .build()
        );

        return etatDemandeMapper.toDto(etatDemandeRepository.save(entity));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public EtatDemandeDTO updateEtatDemande(EtatDemandeDTO etatDemande) {
        ValidationUtils.requirePositiveId(etatDemande.getId(), "id", ENTITY);
        ValidationUtils.requireNonBlank(etatDemande.getCode(), "code", ENTITY);
        ValidationUtils.requireNonBlank(etatDemande.getLibelle(), "libelle", ENTITY);
        ValidationUtils.requireMinLength(etatDemande.getCode(), 2, "code", ENTITY);
        ValidationUtils.requireMinLength(etatDemande.getLibelle(), 2, "libelle", ENTITY);

        var existing = etatDemandeRepository.findById(etatDemande.getId())
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("etat_demande_introuvable", ENTITY, "id_inexistant")));

        // Vérification de duplication du code (hors élément courant)
        etatDemandeRepository.findFirstByCode(etatDemande.getCode())
                .filter(e -> !e.getId().equals(etatDemande.getId()))
                .ifPresent(e -> {
                    throw new CustomBadRequestException(
                            new BadRequestAlertException("etat_demande_code_existe", ENTITY, "code_existe"));
                });

        // Vérification de duplication du libellé
        etatDemandeRepository.findFirstByLibelle(etatDemande.getLibelle())
                .filter(e -> !e.getId().equals(etatDemande.getId()))
                .ifPresent(e -> {
                    throw new CustomBadRequestException(
                            new BadRequestAlertException("etat_demande_libelle_existe", ENTITY, "libelle_existe"));
                });

        // Mise à jour
        existing.setCode(etatDemande.getCode());
        existing.setLibelle(etatDemande.getLibelle());

        return etatDemandeMapper.toDto(etatDemandeRepository.save(existing));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEtatDemande(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        var existing = etatDemandeRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("etat_demande_introuvable", ENTITY, "id_inexistant")));

        etatDemandeRepository.delete(existing);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public EtatDemandeDTO findEtatDemandeById(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        return etatDemandeRepository.findById(id)
                .map(etatDemandeMapper::toDto)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("etat_demande_introuvable", ENTITY, "id_inexistant")));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Page<EtatDemandeDTO> readAllEtatDemandes(Pageable pageable) {
        return etatDemandeRepository.findAllEtatDemande(pageable)
                .map(etatDemandeMapper::toDto);
    }

}
