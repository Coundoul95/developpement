package sn.afrilins.net.gestionEnquete.services.implement.parametrage;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sn.afrilins.net.gestionEnquete.domain.demande.DemandeEnquete;
import sn.afrilins.net.gestionEnquete.domain.enquete.Enquete;
import sn.afrilins.net.gestionEnquete.domain.enquete.SourceInfo;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Document;
import sn.afrilins.net.gestionEnquete.exception.BadRequestAlertException;
import sn.afrilins.net.gestionEnquete.exception.CustomBadRequestException;
import sn.afrilins.net.gestionEnquete.repository.demande.DemandeEnqueteRepository;
import sn.afrilins.net.gestionEnquete.repository.enquete.EnqueteRepository;
import sn.afrilins.net.gestionEnquete.repository.enquete.SourceInfoRepository;
import sn.afrilins.net.gestionEnquete.repository.parametrage.DocumentRepository;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.document.request.DocumentUploadRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.document.response.DocumentDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.DocumentAssociationService;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.DocumentStorageService;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.TypeDocumentService;
import sn.afrilins.net.gestionEnquete.util.ValidationUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentAssociationServiceImpl implements DocumentAssociationService {

    final DocumentStorageService documentStorageService;
    final TypeDocumentService typeDocumentService;

    final DemandeEnqueteRepository demandeEnqueteRepository;
    final EnqueteRepository enqueteRepository;
    final SourceInfoRepository sourceInfoRepository;
    final DocumentRepository documentRepository;

    @PersistenceContext
    EntityManager entityManager;

    static final String ENTITY = "document";

    @Override
    public List<DocumentDTO> uploadAndAssociateDocuments(
            Long enqueteId,
            Long demandeId,
            Long sourceId,
            List<DocumentUploadRequestDTO> metadonnees,
            MultipartFile[] documents
    ) {
        if (documents.length != metadonnees.size()) {
            throw new IllegalArgumentException("Chaque document doit avoir sa métadonnée correspondante.");
        }
        if (enqueteId == null && demandeId == null && sourceId == null) {
            throw new CustomBadRequestException(
                    new BadRequestAlertException("L'enqueteId, demandeId ou sourceId, l'un doit avoir une valeur", ENTITY, "donnee_invalide")
            );
        }

        List<DocumentDTO> results = new ArrayList<>();

        // Détermination de l'utilisateur lié
        Long utilisateurId = null;
        Enquete enquete = null;
        DemandeEnquete demande = null;
        SourceInfo source = null;

        if (enqueteId != null) {
            enquete = getEnqueteOrThrow(enqueteId);
            utilisateurId = enquete.getEnqueteur().getId();
        } else if (sourceId != null) {
            source = getSourceOrThrow(sourceId);
            utilisateurId = source.getUtilisateur().getId();
        } else if (demandeId != null) {
            demande = getDemandeOrThrow(demandeId);
            utilisateurId = demande.getUtilisateur().getId();
        }

        if (Objects.isNull(utilisateurId)) {
            throw new CustomBadRequestException(
                    new BadRequestAlertException("Impossible de déterminer l'utilisateur lié au document", ENTITY, "utilisateur_introuvable")
            );
        }

        // Upload et association
        for (int i = 0; i < documents.length; i++) {
            MultipartFile file = documents[i];
            DocumentUploadRequestDTO meta = metadonnees.get(i);

            Long typeId = typeDocumentService.findTypeDocumentByCode(meta.getCodeType()).getId();

            DocumentDTO docDto = documentStorageService.handleUpload(
                    file,
                    meta.getNom(),
                    meta.getDescription(),
                    typeId,
                    utilisateurId
            );
            results.add(docDto);

            // Récupérer l'entité Document persistée
            Document document = documentRepository.findById(docDto.getId())
                    .orElseThrow(() -> new RuntimeException("Document introuvable après création"));

            // Association en fonction du param fourni
            if (enquete != null) {
                enquete.getDocuments().add(document);
                enqueteRepository.save(enquete);
            }
            if (demande != null) {
                demande.getDocuments().add(document);
                demandeEnqueteRepository.save(demande);
            }
            if (source != null) {
                source.getDocuments().add(document);
                sourceInfoRepository.save(source);
            }
        }

        return results;
    }

//    @Override
//    public void deleteDocument(Long id, Long enqueteId, Long demandeId, Long sourceInfoId) {
//        ValidationUtils.requirePositiveId(id, "id", ENTITY);
//
//        // Charger le document avec toutes ses associations
//        var document = documentRepository.findById(id)
//                .orElseThrow(() -> new CustomBadRequestException(
//                        new BadRequestAlertException("document_introuvable", ENTITY, "id_inexistant")));
//
//        // Vérifier si le document est utilisé en dehors du contexte autorisé
//        boolean isUsed = false;
//
//        // Vérification côté enquêtes
//        if (enqueteId != null) {
//            isUsed = document.getEnquetes().stream().anyMatch(e -> !e.getId().equals(enqueteId));
//        } else {
//            isUsed = !document.getEnquetes().isEmpty();
//        }
//
//        // Vérification côté demandes
//        if (!isUsed) {
//            if (demandeId != null) {
//                isUsed = document.getDemandesEnquete().stream().anyMatch(d -> !d.getId().equals(demandeId));
//            } else {
//                isUsed = !document.getDemandesEnquete().isEmpty();
//            }
//        }
//
//        // Vérification côté sources
//        if (!isUsed) {
//            if (sourceInfoId != null) {
//                isUsed = document.getSourceInfos().stream().anyMatch(s -> !s.getId().equals(sourceInfoId));
//            } else {
//                isUsed = !document.getSourceInfos().isEmpty();
//            }
//        }
//
//        if (isUsed) {
//            throw new CustomBadRequestException(
//                    new BadRequestAlertException(
//                            "document_utilise",
//                            ENTITY,
//                            "Impossible de supprimer : le document est utilisé dans une autre entité."
//                    )
//            );
//        }
//
//        // Nettoyer PROPREMENT toutes les associations
////        cleanAssociations(document, enqueteId, demandeId, sourceInfoId);
//        documentRepository.deleteEnqueteAssociations(document.getId());
//        documentRepository.deleteDemandeAssociations(document.getId());
//        documentRepository.deleteSourceAssociations(document.getId());
//
//        // Sauvegarder les entités modifiées
//        saveModifiedEntities(document);
//
//        // Supprimer le document
//        documentStorageService.deleteDocument(document.getId());
//        documentRepository.delete(document);
//    }

    @Override
    @Transactional
    public void deleteDocument(Long id, Long enqueteId, Long demandeId, Long sourceInfoId) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        // Charger le document avec toutes ses associations
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

        // Nettoyer PROPREMENT toutes les associations avant suppression
        cleanAssociationsProper(document, enqueteId, demandeId, sourceInfoId);

        // Forcer la synchronisation avec la base de données
        entityManager.flush();
        entityManager.clear();

        // Supprimer le document physique et de la base
        documentStorageService.deleteDocument(document.getId());
        documentRepository.deleteById(document.getId());
    }


    Enquete getEnqueteOrThrow(Long id) {
        return enqueteRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("enquete_introuvable", ENTITY, "id_inexistant")));
    }

    DemandeEnquete getDemandeOrThrow(Long id) {
        return demandeEnqueteRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("demande_enquete_introuvable", ENTITY, "id_inexistant")));
    }

    SourceInfo getSourceOrThrow(Long id) {
        return sourceInfoRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("source_info_introuvable", ENTITY, "id_inexistant")));
    }


//    private void cleanAssociations(Document document, Long enqueteId, Long demandeId, Long sourceInfoId) {
//        // Nettoyer les enquêtes
//        if (enqueteId != null) {
//            document.getEnquetes().removeIf(e -> e.getId().equals(enqueteId));
//        } else {
//            for (Enquete enquete : new ArrayList<>(document.getEnquetes())) {
//                enquete.getDocuments().remove(document);
//                enqueteRepository.save(enquete);
//            }
//            document.getEnquetes().clear();
//        }
//
//        // Nettoyer les demandes
//        if (demandeId != null) {
//            document.getDemandesEnquete().removeIf(d -> d.getId().equals(demandeId));
//        } else {
//            for (DemandeEnquete demande : new ArrayList<>(document.getDemandesEnquete())) {
//                demande.getDocuments().remove(document);
//                demandeEnqueteRepository.save(demande);
//            }
//            document.getDemandesEnquete().clear();
//        }
//
//        // Nettoyer les sources
//        if (sourceInfoId != null) {
//            document.getSourceInfos().removeIf(s -> s.getId().equals(sourceInfoId));
//        } else {
//            for (SourceInfo source : new ArrayList<>(document.getSourceInfos())) {
//                source.getDocuments().remove(document);
//                sourceInfoRepository.save(source);
//            }
//            document.getSourceInfos().clear();
//        }
//    }
//
//    private void saveModifiedEntities(Document document) {
//        // Sauvegarder les entités modifiées pour synchroniser la base de données
//        document.getEnquetes().forEach(enqueteRepository::save);
//        document.getDemandesEnquete().forEach(demandeEnqueteRepository::save);
//        document.getSourceInfos().forEach(sourceInfoRepository::save);
//    }

    /**
     * Nettoie proprement toutes les associations bidirectionnelles
     * en gérant les deux côtés de chaque relation avant suppression
     */
    private void cleanAssociationsProper(Document document, Long enqueteId, Long demandeId, Long sourceInfoId) {
        log.debug("Nettoyage des associations pour le document ID: {}", document.getId());

        // Nettoyer les associations avec les enquêtes
        if (enqueteId != null) {
            // Cas spécifique : supprimer seulement l'association avec une enquête donnée
            Enquete enqueteToClean = document.getEnquetes().stream()
                    .filter(e -> e.getId().equals(enqueteId))
                    .findFirst()
                    .orElse(null);

            if (enqueteToClean != null) {
                // Supprimer des deux côtés de la relation
                enqueteToClean.getDocuments().remove(document);
                document.getEnquetes().remove(enqueteToClean);
                enqueteRepository.save(enqueteToClean);
            }
        } else {
            // Cas général : supprimer toutes les associations avec les enquêtes
            List<Enquete> enquetesToClean = new ArrayList<>(document.getEnquetes());
            for (Enquete enquete : enquetesToClean) {
                enquete.getDocuments().remove(document);
                enqueteRepository.save(enquete);
            }
            document.getEnquetes().clear();
        }

        // Nettoyer les associations avec les demandes
        if (demandeId != null) {
            // Cas spécifique : supprimer seulement l'association avec une demande donnée
            DemandeEnquete demandeToClean = document.getDemandesEnquete().stream()
                    .filter(d -> d.getId().equals(demandeId))
                    .findFirst()
                    .orElse(null);

            if (demandeToClean != null) {
                // Supprimer des deux côtés de la relation
                demandeToClean.getDocuments().remove(document);
                document.getDemandesEnquete().remove(demandeToClean);
                demandeEnqueteRepository.save(demandeToClean);
            }
        } else {
            // Cas général : supprimer toutes les associations avec les demandes
            List<DemandeEnquete> demandesToClean = new ArrayList<>(document.getDemandesEnquete());
            for (DemandeEnquete demande : demandesToClean) {
                demande.getDocuments().remove(document);
                demandeEnqueteRepository.save(demande);
            }
            document.getDemandesEnquete().clear();
        }

        // Nettoyer les associations avec les sources
        if (sourceInfoId != null) {
            // Cas spécifique : supprimer seulement l'association avec une source donnée
            SourceInfo sourceToClean = document.getSourceInfos().stream()
                    .filter(s -> s.getId().equals(sourceInfoId))
                    .findFirst()
                    .orElse(null);

            if (sourceToClean != null) {
                // Supprimer des deux côtés de la relation
                sourceToClean.getDocuments().remove(document);
                document.getSourceInfos().remove(sourceToClean);
                sourceInfoRepository.save(sourceToClean);
            }
        } else {
            // Cas général : supprimer toutes les associations avec les sources
            List<SourceInfo> sourcesToClean = new ArrayList<>(document.getSourceInfos());
            for (SourceInfo source : sourcesToClean) {
                source.getDocuments().remove(document);
                sourceInfoRepository.save(source);
            }
            document.getSourceInfos().clear();
        }

        // Sauvegarder le document après nettoyage des collections
        documentRepository.save(document);

        log.debug("Nettoyage terminé pour le document ID: {}", document.getId());
    }
}
