package sn.afrilins.net.gestionEnquete.services.implement.enquete;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.afrilins.net.gestionEnquete.domain.enquete.EvenementCalendrier;
import sn.afrilins.net.gestionEnquete.exception.BadRequestAlertException;
import sn.afrilins.net.gestionEnquete.exception.CustomBadRequestException;
import sn.afrilins.net.gestionEnquete.repository.enquete.EvenementCalendrierRepository;
import sn.afrilins.net.gestionEnquete.repository.enquete.TypeEvenementRepository;
import sn.afrilins.net.gestionEnquete.repository.parametrage.UtilisateurRepository;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.EvenementCalendrierDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.request.EvenementCalendrierRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.EvenementCalendrierService;
import sn.afrilins.net.gestionEnquete.services.mapper.enquete.EvenementCalendrierMapper;
import sn.afrilins.net.gestionEnquete.util.ValidationUtils;

import java.time.LocalDate;

@AllArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Transactional
public class EvenementCalendrierServiceImpl implements EvenementCalendrierService {

    final EvenementCalendrierRepository evenementCalendrierRepository;
    final EvenementCalendrierMapper evenementCalendrierMapper;
    final TypeEvenementRepository typeEvenementRepository;
    final UtilisateurRepository utilisateurRepository;

    final String ENTITY = "evenement_calendrier";

    @Override
    public EvenementCalendrierDTO createEvenement(EvenementCalendrierRequestDTO request) {
        ValidationUtils.requireNonBlank(request.getTitre(), "titre", ENTITY);
        ValidationUtils.requireNonBlank(request.getHeure(), "heure", ENTITY);
        ValidationUtils.requireNonBlank(request.getPriorite(), "priorite", ENTITY);
        ValidationUtils.requireNonNull(request.getDate(), "date", ENTITY);
        ValidationUtils.requirePositiveId(request.getUtilisateurId(), "utilisateurId", ENTITY);
        ValidationUtils.requireNonBlank(request.getTypeCode(), "typeCode", ENTITY);

       evenementCalendrierRepository.findFirstByTitre(request.getTitre()).ifPresent(e -> {
            throw new CustomBadRequestException(
                    new BadRequestAlertException("evenement_calendrier_titre_existe", ENTITY, "titre_existe")
            );
        });


        var utilisateur = utilisateurRepository.findById(request.getUtilisateurId())
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("utilisateur_introuvable", ENTITY, "utilisateur_inexistant")));

        var type = typeEvenementRepository.findFirstByCode(request.getTypeCode())
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("type_introuvable", ENTITY, "type_inexistant")));


        var entity = EvenementCalendrier.builder()
                        .description(request.getDescription())
                        .heure(request.getHeure())
                        .type(type)
                        .duree(request.getDuree())
                        .date(request.getDate())
                        .utilisateur(utilisateur)
                        .priorite(request.getPriorite())
                        .titre(request.getTitre())
                        .build();

        entity.setUtilisateur(utilisateur);
        entity.setType(type);

        return evenementCalendrierMapper.toDto(evenementCalendrierRepository.save(entity));
    }

    @Override
    public EvenementCalendrierDTO updateEvenement(Long id, EvenementCalendrierRequestDTO dto) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        var existing = evenementCalendrierRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("evenement_introuvable", ENTITY, "id_inexistant")));

        if (dto.getTitre() != null && !dto.getTitre().isBlank()) {
            ValidationUtils.requireNonBlank(dto.getTitre(), "titre", ENTITY);
            existing.setTitre(dto.getTitre());
        }

        if (dto.getHeure() != null && !dto.getHeure().isBlank()) {
            ValidationUtils.requireNonBlank(dto.getHeure(), "heure", ENTITY);
            existing.setHeure(dto.getHeure());
        }

        if (dto.getDate() != null) {
            ValidationUtils.requireNonNull(dto.getDate(), "date", ENTITY);
            existing.setDate(dto.getDate());
        }

        if (dto.getDuree() != null) {
            existing.setDuree(dto.getDuree());
        }

        if (dto.getPriorite() != null && !dto.getPriorite().isBlank()) {
            ValidationUtils.requireNonBlank(dto.getPriorite(), "priorite", ENTITY);
            existing.setPriorite(dto.getPriorite());
        }

        if (dto.getDescription() != null) {
            existing.setDescription(dto.getDescription());
        }

        if (dto.getUtilisateurId() != null) {
            ValidationUtils.requirePositiveId(dto.getUtilisateurId(), "utilisateurId", ENTITY);
            var utilisateur = utilisateurRepository.findById(dto.getUtilisateurId())
                    .orElseThrow(() -> new CustomBadRequestException(
                            new BadRequestAlertException("utilisateur_introuvable", ENTITY, "utilisateur_inexistant")));
            existing.setUtilisateur(utilisateur);
        }

        if (dto.getTypeCode() != null && !dto.getTypeCode().isBlank()) {
            ValidationUtils.requireNonBlank(dto.getTypeCode(), "typeCode", ENTITY);
            var type = typeEvenementRepository.findFirstByCode(dto.getTypeCode())
                    .orElseThrow(() -> new CustomBadRequestException(
                            new BadRequestAlertException("type_introuvable", ENTITY, "type_inexistant")));
            existing.setType(type);
        }

        return evenementCalendrierMapper.toDto(evenementCalendrierRepository.save(existing));
    }


    @Override
    public void deleteEvenement(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);
        var existing = evenementCalendrierRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("evenement_introuvable", ENTITY, "id_inexistant")));

        evenementCalendrierRepository.delete(existing);
    }

    @Override
    public EvenementCalendrierDTO findEvenementById(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);
        return evenementCalendrierRepository.findById(id)
                .map(evenementCalendrierMapper::toDto)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("evenement_introuvable", ENTITY, "id_inexistant")));
    }

    @Override
    public Page<EvenementCalendrierDTO> readAllEvenements(String search, String titre, String heure, Integer duree, String priorite, LocalDate date, Long utilisateurId, String typeCode, Pageable pageable) {
        return evenementCalendrierRepository
                .findAllEvenementCalendrier(search, titre, heure, duree, priorite, date, utilisateurId, typeCode, pageable)
                .map(evenementCalendrierMapper::toDto);
    }
}
