package sn.afrilins.net.gestionEnquete.services.implement.demande;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sn.afrilins.net.gestionEnquete.domain.demande.Concerne;
import sn.afrilins.net.gestionEnquete.domain.demande.DemandeEnquete;
import sn.afrilins.net.gestionEnquete.domain.demande.EtatDemande;
import sn.afrilins.net.gestionEnquete.domain.enume.TypeConcerne;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Document;
import sn.afrilins.net.gestionEnquete.domain.parametrage.TypeDocument;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Utilisateur;
import sn.afrilins.net.gestionEnquete.exception.BadRequestAlertException;
import sn.afrilins.net.gestionEnquete.exception.CustomBadRequestException;
import sn.afrilins.net.gestionEnquete.repository.demande.ConcerneRepository;
import sn.afrilins.net.gestionEnquete.repository.demande.DemandeEnqueteRepository;
import sn.afrilins.net.gestionEnquete.repository.demande.EtatDemandeRepository;
import sn.afrilins.net.gestionEnquete.repository.parametrage.DocumentRepository;
import sn.afrilins.net.gestionEnquete.repository.parametrage.TypeDocumentRepository;
import sn.afrilins.net.gestionEnquete.repository.parametrage.UtilisateurRepository;
import sn.afrilins.net.gestionEnquete.services.dto.demande.DemandeEnqueteDTO;
import sn.afrilins.net.gestionEnquete.services.dto.demande.request.DemandeEnqueteRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.demande.request.DemandeEnqueteUpdateRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.DocumentDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.demande.ConcerneService;
import sn.afrilins.net.gestionEnquete.services.interfaces.demande.DemandeEnqueteService;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.EnqueteService;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.DocumentService;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.DocumentStorageService;
import sn.afrilins.net.gestionEnquete.services.mapper.demande.ConcerneMapper;
import sn.afrilins.net.gestionEnquete.services.mapper.demande.DemandeEnqueteMapper;
import sn.afrilins.net.gestionEnquete.services.mapper.parametrage.DocumentMapper;
import sn.afrilins.net.gestionEnquete.util.ValidationUtils;

import java.time.LocalDateTime;


@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DemandeEnqueteServiceImpl implements DemandeEnqueteService {

    final DemandeEnqueteRepository demandeEnqueteRepository;
    final EtatDemandeRepository etatDemandeRepository;
    final ConcerneRepository concerneRepository;
    final UtilisateurRepository utilisateurRepository;
    final DemandeEnqueteMapper demandeEnqueteMapper;
    final EnqueteService enqueteService;
    final ConcerneService concerneService;
    final ConcerneMapper concerneMapper;
    final DocumentRepository documentRepository;
    final DocumentStorageService documentStorageService;
    final DocumentMapper documentMapper;
    final TypeDocumentRepository typeDocumentRepository;

    static final String ENTITY = "demande_enquete";
    static final String ETAT_EN_ATTENTE = "00";
    static final String ETAT_VALIDEE = "01";
    static final String ETAT_ANNULEE = "02";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DemandeEnqueteDTO createDemandeEnquete(DemandeEnqueteRequestDTO dto) {
        log.info("Creating DemandeEnquete for user: {}", dto.getUtilisateurId());

        validateDemande(dto);

        Utilisateur utilisateur = getUtilisateurOrThrow(dto.getUtilisateurId());

        Concerne concerne = resolveConcerne(dto);

        EtatDemande etat = etatDemandeRepository.findFirstByCode(ETAT_EN_ATTENTE)
                .orElseGet(() -> {
                    log.info("Etat '{}' not found. Creating new one.", ETAT_EN_ATTENTE);
                    return etatDemandeRepository.save(EtatDemande.builder()
                            .code(ETAT_EN_ATTENTE)
                            .libelle("En attente")
                            .build());
                });

        DemandeEnquete entity = DemandeEnquete.builder()
                .description(dto.getDescription())
                .etat(etat)
                .urgent(dto.getUrgent())
                .priorite(dto.getPriorite())
                .dateEcheance(dto.getDateEcheance())
                .utilisateur(utilisateur)
                .objet(dto.getObjet())
                .concerne(concerne)
                .build();

        return demandeEnqueteMapper.toDto(demandeEnqueteRepository.save(entity));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DemandeEnqueteDTO updateDemandeEnquete(Long id, DemandeEnqueteUpdateRequestDTO dto) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);
        DemandeEnquete entity = getDemandeOrThrow(id);

        dto.getObjet().ifPresent(obj -> {
            ValidationUtils.requireNonBlank(obj, "objet", ENTITY);
            ValidationUtils.requireMinLength(obj, 3, "objet", ENTITY);
            entity.setObjet(obj);
        });

        dto.getPriorite().ifPresent(priorite -> {
            ValidationUtils.requireInRange(priorite, 1, 5, "priorite", ENTITY);
            entity.setPriorite(priorite);
        });

        dto.getUrgent().ifPresent(entity::setUrgent);

        dto.getCommentaireValidation().ifPresent(commentaire -> {
            ValidationUtils.requireNonBlank(commentaire, "commentaireValidation", ENTITY);
            entity.setCommentaireValidation(commentaire);
        });

        dto.getConcerneId().ifPresent(concerneId -> {
            ValidationUtils.requirePositiveId(concerneId, "concerneId", ENTITY);
            entity.setConcerne(getConcerneOrThrow(concerneId));
        });

        dto.getEtatCode().ifPresent(code -> updateEtat(entity, code));

        DemandeEnquete entitySaved = demandeEnqueteRepository.save(entity);

        if (ETAT_VALIDEE.equals(entitySaved.getEtat().getCode())) {
            enqueteService.createEnquete(entitySaved.getId());
        }

        return demandeEnqueteMapper.toDto(entitySaved);
    }

    @Override
    public void deleteDemandeEnquete(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);
        var entity = getDemandeOrThrow(id);
        demandeEnqueteRepository.delete(entity);
    }

    @Override
    public DemandeEnqueteDTO findDemandeEnqueteById(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);
        return demandeEnqueteMapper.toDto(getDemandeOrThrow(id));
    }

    @Override
    public Page<DemandeEnqueteDTO> findAllDemandeEnquete(Long utilisateurId, Boolean urgent, String objet, String type, String etat, String etatEnquete, Integer priorite, String search, Pageable pageable) {
        return demandeEnqueteRepository.findAllDemandeEnquete(utilisateurId,urgent, objet, type, etat, etatEnquete, priorite, search, pageable).map(demandeEnqueteMapper::toDto) ;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public DemandeEnqueteDTO changerEtatDemandeEnquete(Long demandeId, String nouvelEtatCode) {
        ValidationUtils.requirePositiveId(demandeId, "demande_id", ENTITY);
        ValidationUtils.requireNonBlank(nouvelEtatCode, "etat", ENTITY);
        ValidationUtils.requireMinLength(nouvelEtatCode, 2, "etat", ENTITY);

        DemandeEnquete demande = getDemandeOrThrow(demandeId);

        if (ETAT_VALIDEE.equals(demande.getEtat().getCode()) || ETAT_ANNULEE.equals(demande.getEtat().getCode())) {
            throw new CustomBadRequestException(new BadRequestAlertException("Impossible de changer l'état", ENTITY, "etatCode"));
        }

        updateEtat(demande, nouvelEtatCode);

        var entitySaved = demandeEnqueteRepository.save(demande);

        if (ETAT_VALIDEE.equals(entitySaved.getEtat().getCode()) && entitySaved.getEnquete() == null) {
            enqueteService.createEnquete(entitySaved.getId());
            entitySaved = demandeEnqueteRepository.getById(entitySaved.getId());
        }

        return demandeEnqueteMapper.toDto(entitySaved);
    }

    @Override
    public DemandeEnqueteDTO createDemandeEnqueteAvecDocuments(DemandeEnqueteRequestDTO dto, MultipartFile[] files) {

        var demandeDto = createDemandeEnquete(dto);
        var entity = demandeEnqueteRepository.getById((demandeDto.getId()));

        var type = typeDocumentRepository.findFirstByCode("00")
                .orElseGet(() -> {
                    log.info("Type document '00' not found. Creating new one.");
                    return typeDocumentRepository.save(TypeDocument.builder()
                            .code(ETAT_EN_ATTENTE)
                            .libelle("Piéce joint demande enquête")
                            .build());
                });

        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                DocumentDTO uploadedDoc = documentStorageService.handleUpload(
                        file,
                        file.getOriginalFilename(),
                        null,
                        type.getCode()
                );

                Document docEntity = documentMapper.toEntity(uploadedDoc);
                docEntity.setDemandeEnquete(entity);
                documentRepository.save(docEntity);
            }
        }

        return demandeDto;
    }

    // ====================== Méthodes privées ==============================

    private Concerne resolveConcerne(DemandeEnqueteRequestDTO dto) {
        if (dto.getConcerneId() != null) {
            return getConcerneOrThrow(dto.getConcerneId());
        } else if (dto.getConcerne() != null) {
            return concerneMapper.toEntity(concerneService.createConcerne(dto.getConcerne()));
        } else {
            throw new CustomBadRequestException(new BadRequestAlertException(
                    "Vous devez fournir soit un concerneId soit un concerne.", ENTITY, "concerne"));
        }
    }

    private void updateEtat(DemandeEnquete entity, String etatCode) {
        if (ETAT_VALIDEE.equals(entity.getEtat().getCode()) || ETAT_ANNULEE.equals(entity.getEtat().getCode())) {
            throw new CustomBadRequestException(new BadRequestAlertException("État non modifiable", ENTITY, "etatCode"));
        }

        EtatDemande etat = getEtatOrThrow(etatCode);
        entity.setEtat(etat);
        entity.setDateAnnulation(null);
        entity.setDateValidation(null);

        if (ETAT_VALIDEE.equals(etatCode)) {
            entity.setDateValidation(LocalDateTime.now());
        } else if (ETAT_ANNULEE.equals(etatCode)) {
            entity.setDateAnnulation(LocalDateTime.now());
        }
    }

    private void validateDemande(DemandeEnqueteRequestDTO dto) {
        ValidationUtils.requireNonBlank(dto.getObjet(), "objet", ENTITY);
        ValidationUtils.requireMinLength(dto.getObjet(), 3, "objet", ENTITY);
        ValidationUtils.requireInRange(dto.getPriorite(), 1, 5, "priorite", ENTITY);
        ValidationUtils.requireNonNull(dto.getDateEcheance(), "date_echeance", ENTITY);
        ValidationUtils.requireNonNull(dto.getUtilisateurId(), "utilisateur_id", ENTITY);

        if (dto.getConcerneId() != null && dto.getConcerne() != null) {
            throw new CustomBadRequestException(new BadRequestAlertException("Fournir soit concerneId soit concerne, pas les deux.", ENTITY, "concerne"));
        }

        if (dto.getConcerneId() == null && dto.getConcerne() == null) {
            throw new CustomBadRequestException(new BadRequestAlertException("concerne requis", ENTITY, "concerne"));
        }
    }

    private DemandeEnquete getDemandeOrThrow(Long id) {
        return demandeEnqueteRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("demande_enquete_introuvable", ENTITY, "id_inexistant")));
    }

    private EtatDemande getEtatOrThrow(String code) {
        return etatDemandeRepository.findFirstByCode(code)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("etat_introuvable", ENTITY, "etat_inexistant")));
    }

    private Utilisateur getUtilisateurOrThrow(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("utilisateur_introuvable", ENTITY, "utilisateurId_invalide")));
    }

    private Concerne getConcerneOrThrow(Long id) {
        return concerneRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("concerne_introuvable", ENTITY, "concerneId_invalide")));
    }
}
