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
import sn.afrilins.net.gestionEnquete.domain.enquete.SourceInfo;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Document;
import sn.afrilins.net.gestionEnquete.domain.parametrage.TypeDocument;
import sn.afrilins.net.gestionEnquete.exception.BadRequestAlertException;
import sn.afrilins.net.gestionEnquete.exception.CustomBadRequestException;
import sn.afrilins.net.gestionEnquete.repository.enquete.EnqueteRepository;
import sn.afrilins.net.gestionEnquete.repository.parametrage.UtilisateurRepository;
import sn.afrilins.net.gestionEnquete.repository.enquete.EtatSourceInfoRepository;
import sn.afrilins.net.gestionEnquete.repository.enquete.SourceInfoRepository;
import sn.afrilins.net.gestionEnquete.repository.enquete.TypeSourceRepository;
import sn.afrilins.net.gestionEnquete.repository.parametrage.DocumentRepository;
import sn.afrilins.net.gestionEnquete.repository.parametrage.TypeDocumentRepository;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.source_info.response.SourceInfoDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.source_info.request.SourceInfoRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.source_info.request.SourceInfoUpdateRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.document.response.DocumentDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.SourceInfoService;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.DocumentStorageService;
import sn.afrilins.net.gestionEnquete.services.mapper.enquete.SourceInfoMapper;
import sn.afrilins.net.gestionEnquete.util.ValidationUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
//
//@AllArgsConstructor
//@Slf4j
//@Service
//@FieldDefaults(level = AccessLevel.PACKAGE)
//@Transactional
//public class SourceInfoServiceImpl implements SourceInfoService {
//
//    final SourceInfoRepository sourceInfoRepository;
//    final EtatSourceInfoRepository etatSourceInfoRepository;
//    final UtilisateurRepository utilisateurRepository;
//    final DocumentRepository documentRepository;
//    final SourceInfoMapper sourceInfoMapper;
//    final DocumentStorageService documentStorageService;
//    final TypeDocumentRepository typeDocumentRepository;
//    final TypeSourceRepository typeSourceRepository;
//    final EnqueteRepository enqueteRepository;
//
//    final String ENTITY = "source_info";
//
//    @Override
//    public SourceInfoDTO createSourceInfo(SourceInfoRequestDTO sourceInfo, MultipartFile[] files) {
//        ValidationUtils.requireNonBlank(sourceInfo.getNom(), "nom", ENTITY);
//        ValidationUtils.requireMinLength(sourceInfo.getNom(), 2, "nom", ENTITY);
//        ValidationUtils.requireInRange(sourceInfo.getFiabilite(), 1, 5,"fiabilite", ENTITY);
//        ValidationUtils.requireNonBlank(sourceInfo.getCodeEtat(), "etat", ENTITY);
//        ValidationUtils.requireNonBlank(sourceInfo.getCodeType(), "type", ENTITY);
//        ValidationUtils.requirePositiveId(sourceInfo.getUtilisateurId(), "utilisateurId", ENTITY);
//
//
//        var etat = etatSourceInfoRepository.findFirstByCode(sourceInfo.getCodeEtat())
//                .orElseThrow(() -> new CustomBadRequestException(
//                        new BadRequestAlertException("etat_introuvable", ENTITY, "etatId_invalide")));
//
//        var type = typeSourceRepository.findFirstByCode(sourceInfo.getCodeType())
//                .orElseThrow(() -> new CustomBadRequestException(
//                        new BadRequestAlertException("etat_introuvable", ENTITY, "etatId_invalide")));
//
//        var utilisateur = utilisateurRepository.findById(sourceInfo.getUtilisateurId())
//                .orElseThrow(() -> new CustomBadRequestException(
//                        new BadRequestAlertException("utilisateur_introuvable", ENTITY, "utilisateurId_invalide")));
//
//        List<Document> documents = sourceInfo.getDocumentIds() != null
//                ? documentRepository.findAllById(sourceInfo.getDocumentIds())
//                : java.util.Collections.emptyList();
//
//        List<Enquete> enquetes = sourceInfo.getEnqueteIds() != null
//                ? enqueteRepository.findAllById(sourceInfo.getEnqueteIds())
//                : java.util.Collections.emptyList();
//
//
//        var entity = SourceInfo.builder()
//                .nom(sourceInfo.getNom())
//                .description(sourceInfo.getDescription())
//                .commentaires(sourceInfo.getCommentaires())
//                .fiabilite(sourceInfo.getFiabilite())
//                .dateObtention(sourceInfo.getDateObtention() != null ? sourceInfo.getDateObtention() : LocalDateTime.now())
//                .dateMiseAJour(sourceInfo.getDateMiseAJour() != null ? sourceInfo.getDateMiseAJour() : LocalDateTime.now())
//                .etat(etat)
//                .type(type)
//                .utilisateur(utilisateur)
//                .build();
//
//        if (entity.getDateObtention().isAfter(LocalDateTime.now())) {
//            throw new CustomBadRequestException(
//                    new BadRequestAlertException("date_obtention_futur", ENTITY, "date_invalide")
//            );
//        }
//        if (entity.getDateMiseAJour().isAfter(LocalDateTime.now())) {
//            throw new CustomBadRequestException(
//                    new BadRequestAlertException("date_mis_a_jour_futur", ENTITY, "date_invalide")
//            );
//        }
//
//        // Ajout des documents avec la méthode utilitaire
//        for (Document doc : documents) {
//            entity.addDocument(doc);
//        }
//
//        // Ajout des enquêtes avec cohérence inverse
//        for (Enquete enquete : enquetes) {
//            entity.getEnquetes().add(enquete);
//            enquete.getSourcesInfos().add(entity);
//        }
//
//        return sourceInfoMapper.toDto(sourceInfoRepository.save(entity));
//    }
//
//    @Override
//    public SourceInfoDTO updateSourceInfo(Long id, SourceInfoUpdateRequestDTO sourceInfo) {
//
//        // Vérifie que l'id est valide
//        ValidationUtils.requirePositiveId(id, "id", ENTITY);
//
//        // Récupère l'entité existante
//        var existing = sourceInfoRepository.findById(id)
//                .orElseThrow(() -> new CustomBadRequestException(
//                        new BadRequestAlertException("source_info_introuvable", ENTITY, "id_inexistant")));
//
//        // Nom (optionnel)
//        sourceInfo.getNom().ifPresent(nom -> {
//            ValidationUtils.requireNonBlank(nom, "nom", ENTITY);
//            ValidationUtils.requireMinLength(nom, 2, "nom", ENTITY);
//            existing.setNom(nom);
//        });
//
//        // Description (optionnelle)
//        sourceInfo.getDescription().ifPresent(existing::setDescription);
//
//        // Commentaires (optionnels)
//        sourceInfo.getCommentaires().ifPresent(existing::setCommentaires);
//
//        // Fiabilité (optionnelle)
//        sourceInfo.getFiabilite().ifPresent(f -> {
//            ValidationUtils.requireInRange(f, 1, 5, "fiabilite", ENTITY);
//            existing.setFiabilite(f);
//        });
//
//        // Etat source info (optionnel)
//        sourceInfo.getCodeEtat().ifPresent(code -> {
//            ValidationUtils.requireNonBlank(code, "codeEtat", ENTITY);
//            var etat = etatSourceInfoRepository.findFirstByCode(code)
//                    .orElseThrow(() -> new CustomBadRequestException(
//                            new BadRequestAlertException("etat_source_info_introuvable", ENTITY, "id_inexistant")));
//            existing.setEtat(etat);
//        });
//
//        // Sauvegarde et retourne le DTO
//        return sourceInfoMapper.toDto(sourceInfoRepository.save(existing));
//    }
//
//
//    @Override
//    public void deleteSourceInfo(Long id) {
//        ValidationUtils.requirePositiveId(id, "id", ENTITY);
//
//        var existing = sourceInfoRepository.findById(id)
//                .orElseThrow(() -> new CustomBadRequestException(
//                        new BadRequestAlertException("source_info_introuvable", ENTITY, "id_inexistant")));
//
//        sourceInfoRepository.delete(existing);
//    }
//
//    @Override
//    public SourceInfoDTO findSourceInfoById(Long id) {
//        ValidationUtils.requirePositiveId(id, "id", ENTITY);
//
//        return sourceInfoRepository.findById(id)
//                .map(sourceInfoMapper::toDto)
//                .orElseThrow(() -> new CustomBadRequestException(
//                        new BadRequestAlertException("source_info_introuvable", ENTITY, "id_inexistant")));
//    }
//
//    @Override
//    public Page<SourceInfoDTO> readAllSourceInfo(Long utilisateurId, String nom, String niveauFiabilite,  String etat, String type, String search, Pageable pageable) {
//        return  this.sourceInfoRepository.readAllSourceInfo(utilisateurId, nom, niveauFiabilite, etat, type, search, pageable).map(sourceInfoMapper::toDto);
//    }
//
//    @Override
//    public SourceInfoDTO createSourceInfoWithFiles(SourceInfoRequestDTO requestDTO, List<MultipartFile> files) {
//        final String nom = requestDTO.getNom();
//
//        var type = typeDocumentRepository.findFirstByCode("SOURCE")
//                .orElseThrow(() -> new CustomBadRequestException(
//                        new BadRequestAlertException("type_document_introuvable", ENTITY, "code_inexistant")));
//
//        if (files != null && !files.isEmpty()) {
//            List<DocumentDTO> uploadedDocs = files.stream()
//                    .map(file -> documentStorageService.handleUpload(file, nom, "", type.getId(), requestDTO.getUtilisateurId()))
//                    .collect(Collectors.toList());
//
//            List<Long> documentIds = uploadedDocs.stream()
//                    .map(DocumentDTO::getId)
//                    .collect(Collectors.toList());
//
//            requestDTO.setDocumentIds(documentIds);
//        }
//
////        return createSourceInfo(requestDTO);
//        return null;
//    }
//
//}
@AllArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PACKAGE)
@Transactional
public class SourceInfoServiceImpl implements SourceInfoService {

    final SourceInfoRepository sourceInfoRepository;
    final EtatSourceInfoRepository etatSourceInfoRepository;
    final UtilisateurRepository utilisateurRepository;
    final DocumentRepository documentRepository;
    final SourceInfoMapper sourceInfoMapper;
    final DocumentStorageService documentStorageService;
    final TypeDocumentRepository typeDocumentRepository;
    final TypeSourceRepository typeSourceRepository;
    final EnqueteRepository enqueteRepository;

    final String ENTITY = "source_info";

    /**
     * Création d'une source d'information (sans gestion de fichiers).
     */
    @Override
    public SourceInfoDTO createSourceInfo(SourceInfoRequestDTO sourceInfo) {
        log.debug("Création SourceInfo: {}", sourceInfo);

        // 🔹 Validation du DTO
        validate(sourceInfo);

        // 🔹 Chargement des dépendances obligatoires
        var etat = etatSourceInfoRepository.findFirstByCode(sourceInfo.getCodeEtat())
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("etat_introuvable", ENTITY, "code_etat_invalide")));

        var type = typeSourceRepository.findFirstByCode(sourceInfo.getCodeType())
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("type_introuvable", ENTITY, "code_type_invalide")));

        var utilisateur = utilisateurRepository.findById(sourceInfo.getUtilisateurId())
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("utilisateur_introuvable", ENTITY, "utilisateurId_invalide")));

        // 🔹 Chargement éventuel des documents et enquêtes
        List<Document> documents = (sourceInfo.getDocumentIds() != null)
                ? documentRepository.findAllById(sourceInfo.getDocumentIds())
                : Collections.emptyList();

        List<Enquete> enquetes = (sourceInfo.getEnqueteIds() != null)
                ? enqueteRepository.findAllById(sourceInfo.getEnqueteIds())
                : Collections.emptyList();

        // 🔹 Construction de l’entité
        var entity = SourceInfo.builder()
                .nom(sourceInfo.getNom())
                .description(sourceInfo.getDescription())
                .commentaires(sourceInfo.getCommentaires())
                .fiabilite(sourceInfo.getFiabilite())
                .dateObtention(Optional.ofNullable(sourceInfo.getDateObtention()).orElse(LocalDateTime.now()))
                .dateMiseAJour(Optional.ofNullable(sourceInfo.getDateMiseAJour()).orElse(LocalDateTime.now()))
                .etat(etat)
                .type(type)
                .utilisateur(utilisateur)
                .build();

        // 🔹 Validation des dates
        if (entity.getDateObtention().isAfter(LocalDateTime.now())) {
            throw new CustomBadRequestException(new BadRequestAlertException("date_obtention_future", ENTITY, "date_invalide"));
        }
        if (entity.getDateMiseAJour().isAfter(LocalDateTime.now())) {
            throw new CustomBadRequestException(new BadRequestAlertException("date_mise_a_jour_future", ENTITY, "date_invalide"));
        }

        // 🔹 Ajout relations bidirectionnelles
        documents.forEach(entity::addDocument);
        enquetes.forEach(entity::addEnquete);

        // 🔹 Persistance
        var saved = sourceInfoRepository.save(entity);
        log.info("SourceInfo créé avec ID {}", saved.getId());

        return sourceInfoMapper.toDto(saved);
    }

    /**
     * Mise à jour d'une source d'information (hors fichiers).
     */
    @Override
    public SourceInfoDTO updateSourceInfo(Long id, SourceInfoRequestDTO sourceInfo) {
        log.debug("Mise à jour SourceInfo id={} avec payload {}", id, sourceInfo);

        ValidationUtils.requirePositiveId(id, "id", ENTITY);
        validate(sourceInfo);

        var existing = sourceInfoRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("source_info_introuvable", ENTITY, "id_inexistant")));

        // 🔹 Mise à jour simple des champs
        existing.setNom(sourceInfo.getNom());
        existing.setFiabilite(sourceInfo.getFiabilite());
        existing.setCommentaires(sourceInfo.getCommentaires());
        existing.setDescription(sourceInfo.getDescription());
        existing.setDateMiseAJour(Optional.ofNullable(sourceInfo.getDateMiseAJour()).orElse(LocalDateTime.now()));
        existing.setDateObtention(Optional.ofNullable(sourceInfo.getDateObtention()).orElse(existing.getDateObtention()));

        // 🔹 Chargement des dépendances
        existing.setEtat(etatSourceInfoRepository.findFirstByCode(sourceInfo.getCodeEtat())
                .orElseThrow(() -> new CustomBadRequestException(new BadRequestAlertException("etat_introuvable", ENTITY, "code_etat_invalide"))));

        existing.setType(typeSourceRepository.findFirstByCode(sourceInfo.getCodeType())
                .orElseThrow(() -> new CustomBadRequestException(new BadRequestAlertException("type_introuvable", ENTITY, "code_type_invalide"))));

        // 🔹 Mise à jour des documents
        if (sourceInfo.getDocumentIds() != null) {
            List<Document> newDocs = documentRepository.findAllById(sourceInfo.getDocumentIds());
            existing.getDocuments().forEach(doc -> doc.getSourceInfos().remove(existing));
            existing.getDocuments().clear();
            newDocs.forEach(existing::addDocument);
        }

        // 🔹 Mise à jour des enquêtes
        if (sourceInfo.getEnqueteIds() != null) {
            List<Enquete> newEnquetes = enqueteRepository.findAllById(sourceInfo.getEnqueteIds());
            existing.getEnquetes().forEach(enq -> enq.getSourcesInfos().remove(existing));
            existing.getEnquetes().clear();
            newEnquetes.forEach(existing::addEnquete);
        }

        var saved = sourceInfoRepository.save(existing);
        log.info("SourceInfo id={} mis à jour", id);

        return sourceInfoMapper.toDto(saved);
    }

    @Override
    public void deleteSourceInfo(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        var existing = sourceInfoRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("source_info_introuvable", ENTITY, "id_inexistant")));

        log.info("Suppression SourceInfo id={}", id);
        sourceInfoRepository.delete(existing);
    }

    @Override
    public SourceInfoDTO findSourceInfoById(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        return sourceInfoRepository.findById(id)
                .map(sourceInfoMapper::toDto)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("source_info_introuvable", ENTITY, "id_inexistant")));
    }

    @Override
    public Page<SourceInfoDTO> readAllSourceInfo(Long utilisateurId, String nom, Integer fiabilite,
                                                 String codeEtat, String codeType, Long enqueteId, boolean excludeEnquete, String search, Pageable pageable) {
        return sourceInfoRepository.readAllSourceInfo(utilisateurId, nom, fiabilite, codeEtat, codeType, enqueteId, excludeEnquete, search, pageable)
                .map(sourceInfoMapper::toDto);
    }

    /**
     * Création d'une source avec upload de fichiers.
     */
    @Override
    @Transactional
    public SourceInfoDTO createSourceInfoWithFiles(SourceInfoRequestDTO requestDTO, MultipartFile[] files) {
        return createSourceInfo(addDocumentsSource(requestDTO, files));
    }

    /**
     * Mise à jour d'une source avec upload de fichiers.
     */
    @Override
    @Transactional
    public SourceInfoDTO updateSourceInfoWithFiles(Long id, SourceInfoRequestDTO requestDTO, MultipartFile[] files){
        ValidationUtils.requirePositiveId(id, "id", ENTITY);
        findSourceInfoById(id); // existence
        return updateSourceInfo(id, addDocumentsSource(requestDTO, files));
    }

    /**
     * Ajoute les fichiers uploadés à la liste des documents de la source.
     */
    private SourceInfoRequestDTO addDocumentsSource(SourceInfoRequestDTO requestDTO, MultipartFile[] files){
        log.debug("Ajout fichiers dans source info pour utilisateurId={}", requestDTO.getUtilisateurId());

        // Type de document "PJSOURCE" (créé si inexistant)
        TypeDocument typeDoc = typeDocumentRepository.findFirstByCode("PJSOURCE")
                .orElseGet(() -> typeDocumentRepository.save(TypeDocument.builder()
                        .code("PJSOURCE")
                        .libelle("Pièce jointe source d'information")
                        .build()));

        if (files != null && files.length > 0) {
            List<DocumentDTO> uploadedDocs = new ArrayList<>();

            for (int i = 0; i < files.length; i++) {
                String baseName = requestDTO.getNom();
                String fileName = (files.length > 1) ? baseName + (i + 1) : baseName;

                DocumentDTO doc = documentStorageService.handleUpload(
                        files[i],
                        fileName,
                        Optional.ofNullable(requestDTO.getDescription()).orElse(""),
                        typeDoc.getId(),
                        requestDTO.getUtilisateurId()
                );
                uploadedDocs.add(doc);
            }

            // Merge IDs sans doublons
            Set<Long> allDocIds = new LinkedHashSet<>(
                    Optional.ofNullable(requestDTO.getDocumentIds()).orElse(new ArrayList<>())
            );
            allDocIds.addAll(uploadedDocs.stream().map(DocumentDTO::getId).collect(Collectors.toList()));

            requestDTO.setDocumentIds(new ArrayList<>(allDocIds));
        }
        return requestDTO;
    }

    /**
     * Validation commune des DTO de SourceInfo.
     */
    private void validate(SourceInfoRequestDTO sourceInfo){
        ValidationUtils.requireNonBlank(sourceInfo.getNom(), "nom", ENTITY);
        ValidationUtils.requireMinLength(sourceInfo.getNom(), 2, "nom", ENTITY);
        ValidationUtils.requireInRange(sourceInfo.getFiabilite(), 1, 5,"fiabilite", ENTITY);
        ValidationUtils.requireNonBlank(sourceInfo.getCodeEtat(), "etat", ENTITY);
        ValidationUtils.requireNonBlank(sourceInfo.getCodeType(), "type", ENTITY);
        ValidationUtils.requirePositiveId(sourceInfo.getUtilisateurId(), "utilisateurId", ENTITY);
    }
}
