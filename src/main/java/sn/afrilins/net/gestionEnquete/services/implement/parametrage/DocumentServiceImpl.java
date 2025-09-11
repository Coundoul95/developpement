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
import sn.afrilins.net.gestionEnquete.domain.parametrage.TypeDocument;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Utilisateur;
import sn.afrilins.net.gestionEnquete.exception.BadRequestAlertException;
import sn.afrilins.net.gestionEnquete.exception.CustomBadRequestException;
import sn.afrilins.net.gestionEnquete.repository.parametrage.DocumentRepository;
import sn.afrilins.net.gestionEnquete.repository.parametrage.TypeDocumentRepository;
import sn.afrilins.net.gestionEnquete.repository.parametrage.UtilisateurRepository;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.DocumentDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.request.DocumentRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.DocumentService;
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

       var type =  typeDocumentRepository.findById(documentDTO.getTypeId()).orElseThrow(() -> new CustomBadRequestException(
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
    public void deleteDocument(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);

        var document = documentRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("document_introuvable", ENTITY, "id_inexistant")));

        documentRepository.delete(document);
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
    public Page<DocumentDTO> readAllDocuments(Pageable pageable, String nom, String extension, String type, String categorie, Long utilisateurId) {
        return documentRepository.findAllDocument(pageable, nom, extension, type, categorie, utilisateurId).map(documentMapper::toDto);
    }

    
    private Utilisateur getUtilisateurOrThrow(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("utilisateur_introuvable", ENTITY, "utilisateurId_invalide")));
    }

}
