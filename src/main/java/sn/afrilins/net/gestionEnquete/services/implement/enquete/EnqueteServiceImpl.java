package sn.afrilins.net.gestionEnquete.services.implement.enquete;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.afrilins.net.gestionEnquete.domain.demande.DemandeEnquete;
import sn.afrilins.net.gestionEnquete.domain.demande.EtatDemande;
import sn.afrilins.net.gestionEnquete.domain.enquete.Enquete;
import sn.afrilins.net.gestionEnquete.domain.enquete.EtatEnquete;
import sn.afrilins.net.gestionEnquete.exception.BadRequestAlertException;
import sn.afrilins.net.gestionEnquete.exception.CustomBadRequestException;
import sn.afrilins.net.gestionEnquete.repository.demande.DemandeEnqueteRepository;
import sn.afrilins.net.gestionEnquete.repository.enquete.EnqueteRepository;
import sn.afrilins.net.gestionEnquete.repository.enquete.EtatEnqueteRepository;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.EnqueteDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.EnqueteService;
import sn.afrilins.net.gestionEnquete.services.mapper.enquete.EnqueteMapper;
import sn.afrilins.net.gestionEnquete.util.ValidationUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Transactional
public class EnqueteServiceImpl implements EnqueteService {

    final EnqueteRepository enqueteRepository;
    final EtatEnqueteRepository etatEnqueteRepository;
    final DemandeEnqueteRepository demandeEnqueteRepository;
    final EnqueteMapper enqueteMapper;

    static final String ETAT_EN_ATTENTE = "00";
    static final String ETAT_EN_COURS = "01";
    static  final  String ETAT_TERMINEE = "02";
    static  final  String ETAT_EN_VALIDATION = "03";
    static  final  String ETAT_VALIDEE = "04";
    static  final  String ETAT_EN_REVISION = "05";
    static final String ETAT_ANNULEE = "06";

    static final String ENTITY = "enquete";

    @Override
    public EnqueteDTO createEnquete(Long demandeId) {
        var demande = demandeEnqueteRepository.findById(demandeId).orElseThrow(()->
                new CustomBadRequestException(
                    new BadRequestAlertException("demande_id", ENTITY, "demande_inexistant")
                )
        );
        var etat = etatEnqueteRepository.findFirstByCode("00").orElse(null);
        if(Objects.isNull(etat)){
            etat = etatEnqueteRepository.save(EtatEnquete.builder().code("00").libelle("En attente").build());
        }
        var entity = Enquete.builder().etat(etat).demandeEnquete(demande).progression(0).build();
        return enqueteMapper.toDto(enqueteRepository.save(entity));
    }

    @Override
    public EnqueteDTO updateEnquete(EnqueteDTO dto) {
        return null;
    }

    @Override
    public void deleteEnquete(Long id) {

    }

    @Override
    public EnqueteDTO findEnqueteById(Long id) {
        return null;
    }

    @Override
    public Page<EnqueteDTO> readAllEnquete(String etatCode, Integer progression, LocalDateTime dateDebut, LocalDateTime dateFin, Pageable pageable) {
        return enqueteRepository.readAllEnquete(etatCode, progression, dateDebut, dateFin, pageable).map(enqueteMapper::toDto);
    }


    @Override
    public EnqueteDTO changerEtatEnquete(Long enqueteId, String nouvelEtatCode) {
        ValidationUtils.requirePositiveId(enqueteId, "enquete_id", ENTITY);
        ValidationUtils.requireNonBlank(nouvelEtatCode, "etat", ENTITY);
        ValidationUtils.requireMinLength(nouvelEtatCode, 2, "etat", ENTITY);
        var enquete = getEnqueteOrThrow(enqueteId);
        updateEtat(enquete, nouvelEtatCode);
        return enqueteMapper.toDto(enqueteRepository.save(enquete));
    }

    @Override
    public EnqueteDTO updateProgression(Long enqueteId, int progression) {
        ValidationUtils.requirePositiveId(enqueteId, "enquete_id", ENTITY);
        ValidationUtils.requireInRange(progression,0, 100, "progression", ENTITY);

        var enquete = getEnqueteOrThrow(enqueteId);

        if(!Objects.equals(enquete.getEtat().getCode(), ETAT_EN_COURS)){
            throw new CustomBadRequestException(new BadRequestAlertException(
                    "On ne peut que modifier la progression des enquêtes en cours",
                    ENTITY, "progession"));
        }
        enquete.setProgression(progression);
        enqueteRepository.save(enquete);

        return enqueteMapper.toDto(enquete);
    }



    private void updateEtat(Enquete entity, String etatCode) {
        String currentCode = entity.getEtat().getCode();
        // États terminaux : non modifiables
        if (ETAT_VALIDEE.equals(currentCode) || ETAT_ANNULEE.equals(currentCode)) {
            throw new CustomBadRequestException(new BadRequestAlertException("État non modifiable", ENTITY, "etatCode"));
        }
        if (!isTransitionAutorisee(currentCode, etatCode)) {
            throw new CustomBadRequestException(new BadRequestAlertException(
                    String.format("Transition de l'état %s vers %s interdite", currentCode, etatCode),
                    ENTITY, "etatTransitionInvalid"));
        }
        EtatEnquete nouvelEtat = getEtatOrThrow(etatCode);
        entity.setEtat(nouvelEtat);

        // Réinitialiser dates spécifiques
        entity.setDateAnnulation(null);
        entity.setDateValidation(null);

        // Définir les dates selon l’état cible
        switch (etatCode) {
            case ETAT_VALIDEE:
                entity.setDateValidation(LocalDateTime.now());
                entity.setProgression(100);
                break;
            case ETAT_ANNULEE:
                entity.setDateAnnulation(LocalDateTime.now());
                break;
            case ETAT_TERMINEE:
                entity.setDateFin(LocalDateTime.now());
                entity.setProgression(100);
                break;
            case ETAT_EN_COURS:
                entity.setDateDebut(LocalDateTime.now());
                break;
        }
    }

    private boolean isTransitionAutorisee(String etatActuel, String nouvelEtat) {
        switch (etatActuel) {
            case ETAT_EN_ATTENTE:
                return ETAT_EN_COURS.equals(nouvelEtat) || ETAT_ANNULEE.equals(nouvelEtat);
            case ETAT_EN_COURS:
                return ETAT_TERMINEE.equals(nouvelEtat) || ETAT_EN_VALIDATION.equals(nouvelEtat) || ETAT_ANNULEE.equals(nouvelEtat);
            case ETAT_TERMINEE:
                return ETAT_EN_VALIDATION.equals(nouvelEtat) || ETAT_ANNULEE.equals(nouvelEtat);
            case ETAT_EN_VALIDATION:
                return ETAT_VALIDEE.equals(nouvelEtat) || ETAT_EN_REVISION.equals(nouvelEtat) || ETAT_ANNULEE.equals(nouvelEtat);
            case ETAT_EN_REVISION:
                return ETAT_EN_VALIDATION.equals(nouvelEtat) || ETAT_ANNULEE.equals(nouvelEtat);
            default:
                // ETAT_VALIDEE ou ETAT_ANNULEE => aucune transition permise
                return false;
        }
    }



    private Enquete getEnqueteOrThrow(Long id) {
        return enqueteRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("enquete_introuvable", ENTITY, "id_inexistant")));
    }

    private EtatEnquete getEtatOrThrow(String code) {
        return etatEnqueteRepository.findFirstByCode(code)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("etat_introuvable", ENTITY, "etat_inexistant")));
    }
}
