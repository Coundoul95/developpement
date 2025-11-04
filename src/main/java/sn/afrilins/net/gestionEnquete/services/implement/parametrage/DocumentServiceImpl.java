package sn.afrilins.net.gestionEnquete.services.implement.parametrage;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Document;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Utilisateur;
import sn.afrilins.net.gestionEnquete.exception.BadRequestAlertException;
import sn.afrilins.net.gestionEnquete.exception.CustomBadRequestException;
import sn.afrilins.net.gestionEnquete.repository.parametrage.DocumentRepository;
import sn.afrilins.net.gestionEnquete.repository.parametrage.TypeDocumentRepository;
import sn.afrilins.net.gestionEnquete.repository.parametrage.UtilisateurRepository;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.document.response.DocumentDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.document.request.DocumentRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.document.response.DocumentUsageDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.DocumentService;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.DocumentStorageService;
import sn.afrilins.net.gestionEnquete.services.mapper.parametrage.DocumentMapper;
import sn.afrilins.net.gestionEnquete.util.ValidationUtils;


@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentServiceImpl implements DocumentService {

    final DocumentRepository documentRepository;
    final TypeDocumentRepository typeDocumentRepository;
    final UtilisateurRepository utilisateurRepository;
    final DocumentMapper documentMapper;

    static final String ENTITY = "document";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DocumentDTO createDocument(DocumentRequestDTO documentDTO) {
        ValidationUtils.requireNonBlank(documentDTO.getNom(), "nom", ENTITY);
        ValidationUtils.requirePositive(documentDTO.getTaille(), "taille", ENTITY);
        ValidationUtils.requireNonNull(documentDTO.getTypeId(), "typeId", ENTITY);

        var utilisateur = getUtilisateurOrThrow(documentDTO.getUtilisateurId());

        var type = typeDocumentRepository.findById(documentDTO.getTypeId()).orElseThrow(() -> new CustomBadRequestException(
                new BadRequestAlertException("type_document_existe_pas", "type_document",
                        "type_document_existe_pas")));


        var document = Document.builder()
                .nom(documentDTO.getNom())
                .description(documentDTO.getDescription())
                .chemin(documentDTO.getChemin()) // Accessible via /document/filename
                .extension(documentDTO.getExtension())
                .taille(documentDTO.getTaille())
                .utilisateur(utilisateur)
                .version(documentDTO.getVersion())
                .type(type)
                .build();

        return documentMapper.toDto(documentRepository.save(document));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DocumentDTO updateDocument(DocumentDTO document) {
        ValidationUtils.requirePositiveId(document.getId(), "id", ENTITY);
        ValidationUtils.requireNonBlank(document.getNom(), "nom", ENTITY);
        ValidationUtils.requireNonBlank(document.getChemin(), "chemin", ENTITY);
        ValidationUtils.requireNonBlank(document.getExtension(), "extension", ENTITY);
        ValidationUtils.requirePositive(document.getTaille(), "taille", ENTITY);
        ValidationUtils.requirePositive(document.getVersion(), "version", ENTITY);
        ValidationUtils.requirePositiveId(document.getType().getId(), "typeId", ENTITY);

        var entity = documentRepository.findById(document.getId())
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("document_introuvable", ENTITY, "id_inexistant")));

        var type = typeDocumentRepository.findById(document.getType().getId())
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("type_document_introuvable", ENTITY, "typeId_inexistant")));

        entity.setNom(document.getNom());
        entity.setDescription(document.getDescription());
        entity.setChemin(document.getChemin());
        entity.setExtension(document.getExtension());
        entity.setTaille(document.getTaille());
        entity.setVersion(document.getVersion());
        entity.setType(type);

        return documentMapper.toDto(documentRepository.save(entity));
    }

    @Override
    @Transactional
    public boolean deleteDocument(Long id, Long enqueteId, Long demandeId, Long sourceInfoId) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        var document = documentRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("document_introuvable", ENTITY, "id_inexistant")));

        // Vérifier si le document est utilisé en dehors du contexte autorisé
        boolean isUsed = false;

        // Vérification côté enquêtes
        if (enqueteId != null) {
            isUsed = document.getEnquetes().stream().anyMatch(e -> !e.getId().equals(enqueteId));
        } else {
            isUsed = !document.getEnquetes().isEmpty();
        }

        // Vérification côté demandes
        if (!isUsed) {
            if (demandeId != null) {
                isUsed = document.getDemandesEnquete().stream().anyMatch(d -> !d.getId().equals(demandeId));
            } else {
                isUsed = !document.getDemandesEnquete().isEmpty();
            }
        }

        // Vérification côté sources
        if (!isUsed) {
            if (sourceInfoId != null) {
                isUsed = document.getSourceInfos().stream().anyMatch(s -> !s.getId().equals(sourceInfoId));
            } else {
                isUsed = !document.getSourceInfos().isEmpty();
            }
        }

        if (isUsed) {
            throw new CustomBadRequestException(
                    new BadRequestAlertException(
                            "document_utilise",
                            ENTITY,
                            "Impossible de supprimer : le document est utilisé dans une autre entité."
                    )
            );
        }

        // Détacher toutes les associations avant suppression
        if (enqueteId != null) {
            document.getEnquetes().removeIf(e -> e.getId().equals(enqueteId));
        } else {
            document.getEnquetes().forEach(e -> e.getDocuments().remove(document));
            document.getEnquetes().clear();
        }

        if (demandeId != null) {
            document.getDemandesEnquete().removeIf(d -> d.getId().equals(demandeId));
        } else {
            document.getDemandesEnquete().forEach(d -> d.getDocuments().remove(document));
            document.getDemandesEnquete().clear();
        }

        if (sourceInfoId != null) {
            document.getSourceInfos().removeIf(s -> s.getId().equals(sourceInfoId));
        } else {
            document.getSourceInfos().forEach(s -> s.getDocuments().remove(document));
            document.getSourceInfos().clear();
        }

        // Suppression autorisée
        documentRepository.delete(document);
        return true;
    }


    @Override
    public DocumentDTO findDocumentById(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        return documentRepository.findById(id)
                .map(documentMapper::toDto)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("document_introuvable", ENTITY, "id_inexistant")));
    }


    @Override
    public Page<DocumentDTO> readAllDocuments(
            Pageable pageable, String search, String nom,
            String extension, String type,
            String categorie, Long utilisateurId,
            Long enqueteId, Boolean excludeEnquete,
            Long demandeId, Boolean excludeDemande,
            Long sourceInfoId, Boolean excludeSource) {
        return documentRepository.findAllDocument(
                        pageable, search, nom, extension, type, categorie, utilisateurId,
                        enqueteId, excludeEnquete, demandeId, excludeDemande, sourceInfoId, excludeSource
                )
                .map(documentMapper::toDto);
    }


    @Override
    public DocumentUsageDTO checkIfUsed(Long documentId, Long enqueteId, Long demandeId, Long sourceInfoId) {
        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document non trouvé avec id " + documentId));

        boolean used = false;

        // Vérifie l'usage dans une enquête spécifique ou toutes
        if (enqueteId != null) {
            used = doc.getEnquetes().stream().anyMatch(e -> !e.getId().equals(enqueteId));
        } else {
            used = !doc.getEnquetes().isEmpty();
        }

        // Vérifie l'usage dans une demande spécifique ou toutes
        if (!used) {
            if (demandeId != null) {
                used = doc.getDemandesEnquete().stream().anyMatch(d -> !d.getId().equals(demandeId));
            } else {
                used = !doc.getDemandesEnquete().isEmpty();
            }
        }

        // Vérifie l'usage dans une source spécifique ou toutes
        if (!used) {
            if (sourceInfoId != null) {
                used = doc.getSourceInfos().stream().anyMatch(s -> !s.getId().equals(sourceInfoId));
            } else {
                used = !doc.getSourceInfos().isEmpty();
            }
        }

        String message = used
                ? "Document utilisé ailleurs et ne peut pas être supprimé."
                : "Document non utilisé ou utilisé uniquement dans le contexte spécifié, suppression possible.";

        return DocumentUsageDTO.builder()
                .documentId(documentId)
                .used(used)
                .message(message)
                .build();
    }


    private Utilisateur getUtilisateurOrThrow(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("utilisateur_introuvable", ENTITY, "utilisateurId_invalide")));
    }


}
