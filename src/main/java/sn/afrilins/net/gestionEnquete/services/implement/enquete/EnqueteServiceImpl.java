package sn.afrilins.net.gestionEnquete.services.implement.enquete;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sn.afrilins.net.gestionEnquete.domain.enquete.Enquete;
import sn.afrilins.net.gestionEnquete.domain.enquete.EtatEnquete;
import sn.afrilins.net.gestionEnquete.domain.enquete.SourceInfo;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Document;
import sn.afrilins.net.gestionEnquete.domain.parametrage.TypeDocument;
import sn.afrilins.net.gestionEnquete.exception.BadRequestAlertException;
import sn.afrilins.net.gestionEnquete.exception.CustomBadRequestException;
import sn.afrilins.net.gestionEnquete.repository.demande.DemandeEnqueteRepository;
import sn.afrilins.net.gestionEnquete.repository.enquete.EnqueteRepository;
import sn.afrilins.net.gestionEnquete.repository.enquete.EtatEnqueteRepository;
import sn.afrilins.net.gestionEnquete.repository.enquete.SourceInfoRepository;
import sn.afrilins.net.gestionEnquete.repository.parametrage.DocumentRepository;
import sn.afrilins.net.gestionEnquete.repository.parametrage.TypeDocumentRepository;
import sn.afrilins.net.gestionEnquete.repository.parametrage.UtilisateurRepository;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.request.EnqueteDocumentRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.request.EnqueteSourceRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.response.EnqueteAllDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.response.EnqueteAvecDemandeDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.request.EnqueteAssignationRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.response.EnqueteDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.response.EnqueteStatsDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.EnqueteService;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.DocumentStorageService;
import sn.afrilins.net.gestionEnquete.services.mapper.enquete.EnqueteAllMapper;
import sn.afrilins.net.gestionEnquete.services.mapper.enquete.EnqueteAvecDemandeMapper;
import sn.afrilins.net.gestionEnquete.services.mapper.enquete.EnqueteMapper;
import sn.afrilins.net.gestionEnquete.util.ValidationUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    final EnqueteAvecDemandeMapper enqueteAvecDemandeMapper;
    final UtilisateurRepository utilisateurRepository;
    final EnqueteAllMapper enqueteAllMapper;
    final DocumentRepository documentRepository;
    final TypeDocumentRepository typeDocumentRepository;
    final DocumentStorageService documentStorageService;
    final SourceInfoRepository sourceInfoRepository;

    static final String ETAT_EN_ATTENTE = "00";
    static final String ETAT_EN_COURS = "01";
    static final String ETAT_TERMINEE = "02";
    static final String ETAT_EN_VALIDATION = "03";
    static final String ETAT_VALIDEE = "04";
    static final String ETAT_EN_REVISION = "05";
    static final String ETAT_ANNULEE = "06";

    static final String ENTITY = "enquete";

    @Override
    public EnqueteDTO createEnquete(Long demandeId) {
        var demande = demandeEnqueteRepository.findById(demandeId).orElseThrow(() ->
                new CustomBadRequestException(
                        new BadRequestAlertException("demande_id", ENTITY, "demande_inexistant")
                )
        );
        var etat = etatEnqueteRepository.findFirstByCode(ETAT_EN_ATTENTE).orElse(null);
        if (Objects.isNull(etat)) {
            etat = etatEnqueteRepository.save(EtatEnquete.builder().code(ETAT_EN_ATTENTE).libelle("En attente").build());
        }
        var entity = Enquete.builder().etat(etat).demandeEnquete(demande).progression(0).build();
        entity.setDateDebut(null);
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
    public EnqueteAvecDemandeDTO findEnqueteById(Long id) {
        return enqueteRepository.findById(id).map(enqueteAvecDemandeMapper::toDto).orElseThrow(() ->
                new CustomBadRequestException(
                        new BadRequestAlertException("enquete_introuvable", ENTITY, "id_inexistant"))
        );
    }

    @Override
    public Page<EnqueteAvecDemandeDTO> readAllEnqueteAvecDemande(
            String etatCode,
            Integer progression,
            LocalDateTime dateDebut,
            LocalDateTime dateFin,
            Boolean assignee,
            Long enqueteurId,
            String search,
            String type,
            Integer priorite,
            Boolean urgent,
            Pageable pageable) {
        return enqueteRepository.readAllEnquete(etatCode, progression, dateDebut, dateFin, assignee, enqueteurId, search, type, priorite, urgent, pageable).map(enqueteAvecDemandeMapper::toDto);
    }

    @Override
    public Page<EnqueteDTO> readAllEnqueteSansDemande(
            String etatCode,
            Integer progression,
            LocalDateTime dateDebut,
            LocalDateTime dateFin,
            Boolean assignee,
            Long enqueteurId,
            String search,
            String type,
            Integer priorite,
            Boolean urgent,
            Pageable pageable) {
        return enqueteRepository.readAllEnquete(etatCode, progression, dateDebut, dateFin, assignee, enqueteurId, search, type, priorite, urgent, pageable).map(enqueteMapper::toDto);
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
        ValidationUtils.requireInRange(progression, 0, 100, "progression", ENTITY);

        var enquete = getEnqueteOrThrow(enqueteId);

        if (!Objects.equals(enquete.getEtat().getCode(), ETAT_EN_COURS)) {
            throw new CustomBadRequestException(new BadRequestAlertException(
                    "On ne peut que modifier la progression des enquêtes en cours",
                    ENTITY, "progession"));
        }
        enquete.setProgression(progression);
        enqueteRepository.save(enquete);

        return enqueteMapper.toDto(enquete);
    }


    @Override
    public EnqueteStatsDTO getStatsEtat(Long utilisateurId) {

        List<Object[]> results = enqueteRepository.countEnquetesByEtat(utilisateurId);

        Map<String, Long> counts = results.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                ));

        long total = counts.values().stream().mapToLong(Long::longValue).sum();

        return EnqueteStatsDTO.builder()
                .enAttente(counts.getOrDefault(ETAT_EN_ATTENTE, 0L))
                .enCours(counts.getOrDefault(ETAT_EN_COURS, 0L))
                .terminees(counts.getOrDefault(ETAT_TERMINEE, 0L))
                .enValidation(counts.getOrDefault(ETAT_EN_VALIDATION, 0L))
                .enRevision(counts.getOrDefault(ETAT_EN_REVISION, 0L))
                .valides(counts.getOrDefault(ETAT_VALIDEE, 0L)) // corrigé ici
                .annulees(counts.getOrDefault(ETAT_ANNULEE, 0L))
                .echeances(enqueteRepository.countEcheancesProches(utilisateurId))
                .urgent(enqueteRepository.countEnquetesUrgentes(utilisateurId))
                .progression(enqueteRepository.averageProgression(utilisateurId))
                .total(total)
                .build();
    }

    @Override
    public EnqueteDTO assignerEnqueteur(Long enqueteId, EnqueteAssignationRequestDTO dto) {
        ValidationUtils.requirePositiveId(dto.getEnqueteurId(), "enqueteurId", ENTITY);
        var enquete = getEnqueteOrThrow(enqueteId);
        var enqueteur = utilisateurRepository.findById(dto.getEnqueteurId()).orElseThrow(() ->
                new CustomBadRequestException(new BadRequestAlertException("Enqueteur introuvable", ENTITY, "enqueteur_id_invalide"))
        );
        if (Objects.nonNull(enquete.getEnqueteur())) {
            throw new CustomBadRequestException(new BadRequestAlertException(
                    "Enquête déjà assigné", ENTITY, "enquteAssigné"));
        }
        System.out.println(" Code de l'état de l'enquête " + enquete.getEtat().getCode());
        if (!Objects.equals(enquete.getEtat().getCode(), ETAT_EN_ATTENTE)) {
            throw new CustomBadRequestException(new BadRequestAlertException(
                    "On ne peut pas assigné un enquêteur à cette enquête parce que l'enquête est déjà démarrer", ENTITY, "enquteAssigné"));
        }
        enquete.setEnqueteur(enqueteur);
        enquete.setDateAssignation(LocalDateTime.now());
        if (Objects.nonNull(dto.getInstruction())) {
            enquete.setInstruction(dto.getInstruction());
        }
        if (Objects.nonNull(dto.getDateLimite())) {
            enquete.setDateLimite(dto.getDateLimite());
        }

        return enqueteMapper.toDto(enqueteRepository.save(enquete));
    }

    @Override
    public EnqueteAllDTO findEnqueteByIdAll(Long enqueteId) {
        return enqueteAllMapper.toDto(getEnqueteOrThrow(enqueteId));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public EnqueteAllDTO ajouterDocuments(Long enqueteId,  MultipartFile[] fichiers, EnqueteDocumentRequestDTO request) {
        Enquete enquete = getEnqueteOrThrow(enqueteId);

        // 1. Véfication de l'enquête
        verifedEnquete(enquete);

        // 2. Associer documents existants
        if (request.getIds() != null && !request.getIds().isEmpty()) {
            List<Document> existingDocs = documentRepository.findAllById(request.getIds());

//            existingDocs.stream()
//                    .filter(doc -> Objects.equals(doc.getUtilisateur().getId(), enquete.getEnqueteur().getId()))
//                    .forEach(enquete.getDocuments()::add);

            existingDocs.stream()
                    .filter(doc -> Objects.equals(doc.getUtilisateur().getId(), enquete.getEnqueteur().getId()))
                    .forEach(doc -> {
                        if (!enquete.getDocuments().contains(doc)) {
                            enquete.addDocument(doc); // met à jour les deux côtés
                        }
                    });
        }

        // 3. Gérer upload de nouveaux fichiers
        if (fichiers != null && fichiers.length > 0) {
            TypeDocument type = typeDocumentRepository.findFirstByCode("PJENQ")
                    .orElseGet(() -> {
                        log.info("Type document 'PJENQ' not trouvé. Création...");
                        return typeDocumentRepository.save(TypeDocument.builder()
                                .code("PJENQ")
                                .libelle("Pièce jointe enquête")
                                .build());
                    });

            for (MultipartFile fichier : fichiers) {
                var uploadedDoc = documentStorageService.handleUpload(
                        fichier,
                        fichier.getOriginalFilename(),
                        "Pièce jointe enquête",
                        type.getCode(),
                        enquete.getEnqueteur().getId()
                );

                Document docEntity = documentRepository.getById(uploadedDoc.getId());
                enquete.getDocuments().add(docEntity);
            }
        }

        return enqueteAllMapper.toDto(enqueteRepository.save(enquete));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EnqueteAllDTO ajouterSources(Long enqueteId, EnqueteSourceRequestDTO request) {
        Enquete enquete = getEnqueteOrThrow(enqueteId);

        // 1. Véfication de l'enquête
        verifedEnquete(enquete);

        // 2. Associer source d'information existants
        if (request.getIds() != null && !request.getIds().isEmpty()) {
            List<SourceInfo> existingSources = sourceInfoRepository.findAllById(request.getIds());

            existingSources.stream()
                    .filter(source -> Objects.equals(source.getUtilisateur().getId(), enquete.getEnqueteur().getId()))
                    .forEach(source -> {
                        if (!enquete.getSourcesInfos().contains(source)) {
                            source.addEnquete(enquete); // met à jour les 2 côtés
                        }
                    });

        }

        return enqueteAllMapper.toDto(enqueteRepository.save(enquete));
    }


    private void verifedEnquete(Enquete enquete){
        // 1. Vérifier si l'enquête est assigné
        if (Objects.isNull(enquete.getEnqueteur())) {
            throw new CustomBadRequestException(
                    new BadRequestAlertException("enquete_non_assigne", ENTITY, "enquete_non_assigne")
            );
        }

        // 2. Vérifier si l'enquête est en cours
        if(!enquete.getEtat().getCode().equals(ETAT_EN_COURS)){
            throw new CustomBadRequestException(
                    new BadRequestAlertException("L'enquete n'est pas en cours", ENTITY, "enquete_non_encours")
            );
        }
    }

    private void updateEtat(Enquete entity, String etatCode) {

        if (Objects.isNull(entity.getEnqueteur())) {
            throw new CustomBadRequestException(new BadRequestAlertException("L'enquête n'est pas encore assigné donc on ne peut pas changer l'état", ENTITY, "enqueteur_manquant"));
        }

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

        System.out.println("This is a test: " + entity.getDateDebut());
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
                // ETAT_VALIDEE ou STAT_ANNULEE => aucune transition permise
                return false;
        }
    }


    @Override
    public Enquete getEnqueteOrThrow(Long id) {
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
