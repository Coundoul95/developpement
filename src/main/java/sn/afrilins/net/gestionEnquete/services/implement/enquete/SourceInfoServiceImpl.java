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
import sn.afrilins.net.gestionEnquete.domain.enquete.SourceInfo;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Document;
import sn.afrilins.net.gestionEnquete.exception.BadRequestAlertException;
import sn.afrilins.net.gestionEnquete.exception.CustomBadRequestException;
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
import java.util.List;
import java.util.stream.Collectors;

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

    final String ENTITY = "source_info";

    @Override
    public SourceInfoDTO createSourceInfo(SourceInfoRequestDTO sourceInfo) {
        ValidationUtils.requireNonBlank(sourceInfo.getNom(), "nom", ENTITY);
        ValidationUtils.requireMinLength(sourceInfo.getNom(), 2, "nom", ENTITY);
        ValidationUtils.requireNonBlank(sourceInfo.getNiveauFiabilite(), "niveauFiabilite", ENTITY);
        ValidationUtils.requireNonBlank(sourceInfo.getEtat(), "etat", ENTITY);
        ValidationUtils.requireNonBlank(sourceInfo.getType(), "type", ENTITY);
        ValidationUtils.requirePositiveId(sourceInfo.getUtilisateurId(), "utilisateurId", ENTITY);

        if (sourceInfo.getDateObtention() == null) {
            sourceInfo.setDateObtention(LocalDateTime.now());
        }
        if (sourceInfo.getDateMiseAJour() == null) {
            sourceInfo.setDateMiseAJour(LocalDateTime.now());
        }

        if (sourceInfo.getDateObtention().isAfter(LocalDateTime.now())) {
            throw new CustomBadRequestException(
                    new BadRequestAlertException("date_obtention_futur", ENTITY, "date_invalide")
            );
        }
        if (sourceInfo.getDateMiseAJour().isAfter(LocalDateTime.now())) {
            throw new CustomBadRequestException(
                    new BadRequestAlertException("date_mis_a_jour_futur", ENTITY, "date_invalide")
            );
        }

        var etat = etatSourceInfoRepository.findFirstByCode(sourceInfo.getEtat())
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("etat_introuvable", ENTITY, "etatId_invalide")));

        var type = typeSourceRepository.findFirstByCode(sourceInfo.getType())
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("etat_introuvable", ENTITY, "etatId_invalide")));

        var utilisateur = utilisateurRepository.findById(sourceInfo.getUtilisateurId())
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("utilisateur_introuvable", ENTITY, "utilisateurId_invalide")));

        List<Document> documents = sourceInfo.getDocumentIds() != null
                ? documentRepository.findAllById(sourceInfo.getDocumentIds())
                : java.util.Collections.emptyList();

        var entity = SourceInfo.builder()
                .nom(sourceInfo.getNom())
                .description(sourceInfo.getDescription())
                .commentaires(sourceInfo.getCommentaires())
                .niveauFiabilite(sourceInfo.getNiveauFiabilite())
                .dateObtention(sourceInfo.getDateObtention())
                .dateMiseAJour(sourceInfo.getDateMiseAJour())
                .etat(etat)
                .type(type)
                .utilisateur(utilisateur)
                .documents(documents)
                .build();

        return sourceInfoMapper.toDto(sourceInfoRepository.save(entity));
    }

    @Override
    public SourceInfoDTO updateSourceInfo(SourceInfoUpdateRequestDTO sourceInfo) {
        ValidationUtils.requirePositiveId(sourceInfo.getId(), "id", ENTITY);
        ValidationUtils.requireNonBlank(sourceInfo.getNom(), "nom", ENTITY);
        ValidationUtils.requireNonBlank(sourceInfo.getNiveauFiabilite(), "niveauFiabilite", ENTITY);
        ValidationUtils.requireMinLength(sourceInfo.getNom(), 2, "nom", ENTITY);

        var existing = sourceInfoRepository.findById(sourceInfo.getId())
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("source_info_introuvable", ENTITY, "id_inexistant")));
        if(sourceInfo.getEtatId() != null){
            ValidationUtils.requirePositiveId(sourceInfo.getEtatId(), "etatId", ENTITY);
            existing.setEtat(this.etatSourceInfoRepository.findById(
                    sourceInfo.getEtatId())
                    .orElseThrow(() -> new CustomBadRequestException(
                            new BadRequestAlertException("etat_source_info_introuvable", ENTITY, "id_inexistant"))));
        }

        existing.setNom(sourceInfo.getNom());
        existing.setDescription(sourceInfo.getDescription());
        existing.setCommentaires(sourceInfo.getCommentaires());
        existing.setNiveauFiabilite(sourceInfo.getNiveauFiabilite());

        return sourceInfoMapper.toDto(sourceInfoRepository.save(existing));
    }

    @Override
    public void deleteSourceInfo(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        var existing = sourceInfoRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("source_info_introuvable", ENTITY, "id_inexistant")));

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
    public Page<SourceInfoDTO> readAllSourceInfo(Long utilisateurId, String nom, String niveauFiabilite,  String etat, String type, String search, Pageable pageable) {
        return  this.sourceInfoRepository.readAllSourceInfo(utilisateurId, nom, niveauFiabilite, etat, type, search, pageable).map(sourceInfoMapper::toDto);
    }

    @Override
    public SourceInfoDTO createSourceInfoWithFiles(SourceInfoRequestDTO requestDTO, List<MultipartFile> files) {
        final String nom = requestDTO.getNom();

        var type = typeDocumentRepository.findFirstByCode("SOURCE")
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("type_document_introuvable", ENTITY, "code_inexistant")));

        if (files != null && !files.isEmpty()) {
            List<DocumentDTO> uploadedDocs = files.stream()
                    .map(file -> documentStorageService.handleUpload(file, nom, "", type.getId(), requestDTO.getUtilisateurId()))
                    .collect(Collectors.toList());

            List<Long> documentIds = uploadedDocs.stream()
                    .map(DocumentDTO::getId)
                    .collect(Collectors.toList());

            requestDTO.setDocumentIds(documentIds);
        }

        return createSourceInfo(requestDTO);
    }

}
