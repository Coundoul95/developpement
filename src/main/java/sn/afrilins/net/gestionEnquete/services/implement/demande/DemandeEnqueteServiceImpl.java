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
import sn.afrilins.net.gestionEnquete.domain.enume.EtatDemandeEnqueteCode;
import sn.afrilins.net.gestionEnquete.domain.enume.EtatEnqueteCode;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Document;
import sn.afrilins.net.gestionEnquete.domain.parametrage.TypeDocument;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Utilisateur;
import sn.afrilins.net.gestionEnquete.exception.BadRequestAlertException;
import sn.afrilins.net.gestionEnquete.exception.CustomBadRequestException;
import sn.afrilins.net.gestionEnquete.repository.demande.ConcerneRepository;
import sn.afrilins.net.gestionEnquete.repository.demande.DemandeEnqueteRepository;
import sn.afrilins.net.gestionEnquete.repository.demande.EtatDemandeRepository;
import sn.afrilins.net.gestionEnquete.repository.enquete.EnqueteRepository;
import sn.afrilins.net.gestionEnquete.repository.parametrage.DocumentRepository;
import sn.afrilins.net.gestionEnquete.repository.parametrage.TypeDocumentRepository;
import sn.afrilins.net.gestionEnquete.repository.parametrage.UtilisateurRepository;
import sn.afrilins.net.gestionEnquete.services.dto.demande.demande_enquete.response.*;
import sn.afrilins.net.gestionEnquete.services.dto.demande.demande_enquete.request.DemandeEnqueteRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.demande.demande_enquete.request.DemandeEnqueteUpdateRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.document.response.DocumentDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.demande.ConcerneService;
import sn.afrilins.net.gestionEnquete.services.interfaces.demande.DemandeEnqueteService;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.EnqueteService;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.DocumentStorageService;
import sn.afrilins.net.gestionEnquete.services.mapper.demande.ConcerneMapper;
import sn.afrilins.net.gestionEnquete.services.mapper.demande.DemandeEnqueteMapper;
import sn.afrilins.net.gestionEnquete.services.mapper.parametrage.DocumentMapper;
import sn.afrilins.net.gestionEnquete.util.ValidationUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


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
    final EnqueteRepository enqueteRepository;
    final ConcerneService concerneService;
    final ConcerneMapper concerneMapper;
    final DocumentRepository documentRepository;
    final DocumentStorageService documentStorageService;
    final DocumentMapper documentMapper;
    final TypeDocumentRepository typeDocumentRepository;

    static final String ENTITY = "demande_enquete";
    static final String ETAT_EN_ATTENTE = EtatDemandeEnqueteCode.EN_ATTENTE.getValue();
    static final String ETAT_VALIDEE = EtatDemandeEnqueteCode.VALIDER.getValue();
    static final String ETAT_ANNULEE = EtatDemandeEnqueteCode.ANNULER.getValue();
    static final String ETAT_REJETEE = EtatDemandeEnqueteCode.REJETER.getValue();
    static final String ETAT_EN_COMPLEMENT = EtatDemandeEnqueteCode.EN_COMPLEMENT.getValue();

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


        List<Document> documents = dto.getDocumentIds() != null
                ? documentRepository.findAllById(dto.getDocumentIds())
                : java.util.Collections.emptyList();

        for (Document doc : documents) {
            entity.addDocument(doc);
        }

        log.debug("Test demande créer", entity);

        return demandeEnqueteMapper.toDto(demandeEnqueteRepository.save(entity));
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public DemandeEnqueteDTO updateDemandeEnquete(Long id, DemandeEnqueteUpdateRequestDTO dto) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);
        DemandeEnquete entity = getDemandeOrThrow(id);

        // Mise à jour des champs standards
        dto.getObjet().ifPresent(obj -> {
            ValidationUtils.requireNonBlank(obj, "objet", ENTITY);
            ValidationUtils.requireMinLength(obj, 3, "objet", ENTITY);
            entity.setObjet(obj);
        });

        dto.getDescription().ifPresent(desc -> {
            ValidationUtils.requireNonBlank(desc, "description", ENTITY);
            entity.setDescription(desc);
        });

        dto.getPriorite().ifPresent(priorite -> {
            ValidationUtils.requireInRange(priorite, 1, 5, "priorite", ENTITY);
            entity.setPriorite(priorite);
        });

        dto.getUrgent().ifPresent(entity::setUrgent);
        dto.getDateEcheance().ifPresent(entity::setDateEcheance);
//        dto.getCommentaireValidation().ifPresent(entity::setCommentaireValidation);
        Optional.ofNullable(dto.getCommentaireValidation())
                .flatMap(c -> c) // si dto renvoie déjà Optional
                .ifPresent(entity::setCommentaireValidation);

        dto.getConcerneId().ifPresent(concerneId -> entity.setConcerne(getConcerneOrThrow(concerneId)));

        DemandeEnquete entitySaved = demandeEnqueteRepository.save(entity);


        return demandeEnqueteMapper.toDto(entitySaved);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DemandeEnqueteDTO updateDemandeEnqueteAvecDocuments(Long id, DemandeEnqueteUpdateRequestDTO dto, MultipartFile[] files) {
        // Mise à jour des champs principaux
        DemandeEnqueteDTO updatedDemande = updateDemandeEnquete(id, dto);
        DemandeEnquete entity = demandeEnqueteRepository.getById(updatedDemande.getId());

        // Mise à jour des documents existants
        if (dto.getDocumentIds() != null) {
            List<Document> documents = documentRepository.findAllById(dto.getDocumentIds());
            entity.getDocuments().clear(); // Remplacer ou fusionner selon le besoin
            documents.forEach(entity::addDocument);
        }

        // Upload des nouveaux fichiers
        if (files != null) {
            var type = typeDocumentRepository.findFirstByCode("PJ00")
                    .orElseGet(() -> typeDocumentRepository.save(TypeDocument.builder()
                            .code("PJ00")
                            .libelle("Pièce jointe demande enquête")
                            .build()));

            for (MultipartFile file : files) {
                DocumentDTO uploadedDoc = documentStorageService.handleUpload(
                        file,
                        file.getOriginalFilename(),
                        "Pièce jointe demande enquête",
                        type.getCode(),
                        entity.getUtilisateur().getId()
                );
                Document docEntity = documentRepository.getById(uploadedDoc.getId());
                entity.addDocument(docEntity);
                documentRepository.save(docEntity);
            }
        }

        return demandeEnqueteMapper.toDto(demandeEnqueteRepository.save(entity));
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
        return demandeEnqueteRepository.findAllDemandeEnquete(utilisateurId, urgent, objet, type, etat, etatEnquete, priorite, search, pageable).map(demandeEnqueteMapper::toDto);
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
    @Transactional(rollbackFor = Exception.class)
    public DemandeEnqueteDTO createDemandeEnqueteAvecDocuments(DemandeEnqueteRequestDTO dto, MultipartFile[] files) {

        var demandeDto = createDemandeEnquete(dto);
        var entity = demandeEnqueteRepository.getById(demandeDto.getId());

        var type = typeDocumentRepository.findFirstByCode("PJ00")
                .orElseGet(() -> {
                    log.info("Type document 'PJ00' not found. Creating new one.");
                    return typeDocumentRepository.save(TypeDocument.builder()
                            .code("PJ00")
                            .libelle("Piéce joint demande enquête")
                            .build());
                });

        if (files != null) {
            for (MultipartFile file : files) {
                DocumentDTO uploadedDoc = documentStorageService.handleUpload(
                        file,
                        file.getOriginalFilename(),
                        "Pièce joint demande enquête",
                        type.getCode(),
                        dto.getUtilisateurId()
                );


                Document docEntity = documentRepository.getById(uploadedDoc.getId());
                entity.addDocument(docEntity);
                documentRepository.save(docEntity);
            }
        }


        return demandeEnqueteMapper.toDto(demandeEnqueteRepository.save(entity));
    }


    @Override
    public DemandeEnqueteStatsDTO getStatsEtat(Long utilisateurId) {
        List<Object[]> results = demandeEnqueteRepository.countDemandesByEtat(utilisateurId);

        Map<String, Long> counts = results.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                ));

        long total = counts.values().stream().mapToLong(Long::longValue).sum();

        return DemandeEnqueteStatsDTO.builder()
                .validees(counts.getOrDefault(ETAT_VALIDEE, 0L))
                .enAttentes(counts.getOrDefault(ETAT_EN_ATTENTE, 0L))
                .rejetees(counts.getOrDefault(ETAT_REJETEE, 0L))
                .annulees(counts.getOrDefault(ETAT_ANNULEE, 0L))
                .enComplement(counts.getOrDefault(ETAT_EN_COMPLEMENT, 0L))
                .total(total)
                .build();
    }


    @Override
    public DemandeValideEnqueteStatsDTO getStatsEtatEnquete(Long utilisateurId) {
        List<Object[]> rows = enqueteRepository.getStatsEtatEnquete(utilisateurId);

        if (rows == null || rows.isEmpty()) {
            return new DemandeValideEnqueteStatsDTO(0L, 0L, 0L, 0L);
        }

        Object[] row = rows.get(0);

        return new DemandeValideEnqueteStatsDTO(
                safeLong(row[0]), // totalEnCours
                safeLong(row[1]), // prioriteHaute
                safeLong(row[2]), // enValidation
                safeLong(row[3])  // enRetard
        );
    }


    @Override
    public DemandeStatistiquesDTO getStatistiques(Long utilisateurId) {
        System.out.println("Test utilisateur "+ utilisateurId);
        long total = demandeEnqueteRepository.countByEtatAndUtilisateur(null, utilisateurId);
        long enCours = demandeEnqueteRepository.countByEtatAndUtilisateur(EtatEnqueteCode.EN_COURS.getValue(), utilisateurId);
        long terminees = demandeEnqueteRepository.countByEtatAndUtilisateur(EtatEnqueteCode.TERMINEE.getValue(), utilisateurId);
        double tauxReussite = total > 0 ? (terminees * 100.0 / total) : 0;

        return DemandeStatistiquesDTO.builder()
                .totalDemandes(total)
                .enCours(enCours)
                .terminees(terminees)
                .tauxReussite(tauxReussite)
                .build();
    }


    @Override
    public List<DemandeSerieDTO> getEvolutionDemandes(Long utilisateurId, LocalDate date) {
        LocalDate targetDate = (date != null) ? date : LocalDate.now();
        LocalDateTime startOfWeek = targetDate.minusDays(targetDate.getDayOfWeek().getValue() - 1).atStartOfDay();
        List<Object[]> creees = demandeEnqueteRepository.getDemandesCreeesParJour(utilisateurId, startOfWeek);
        List<Object[]> traitees = demandeEnqueteRepository.getDemandesTraiteesParJour(utilisateurId, startOfWeek);
        return DemandeSerieDTO.fromResults(creees, traitees, targetDate);
    }



    // ====================== Méthodes privées ==============================

    private Long safeLong(Object obj) {
        return obj != null ? ((Number) obj).longValue() : 0L;
    }

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
