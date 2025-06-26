package sn.afrilins.net.gestionEnquete.services.implement.parametrage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.DocumentDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.DocumentDebugInfo;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.DocumentViewUrlDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.request.DocumentRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.DocumentService;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.DocumentStorageService;
import sn.afrilins.net.gestionEnquete.util.AppUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentStorageServiceImpl implements DocumentStorageService {

    private final DocumentService documentService;
    @Value("${app.documents.storage.path}")
    private String documentsStoragePath;

    @Value("${app.api.base-path}")
    private String apiBasePath;

    @Override
    public DocumentDTO handleUpload(MultipartFile file, String nom, String description, Long typeId) {

        String extension = AppUtils.getExtension(file.getOriginalFilename());
        int taille = (int) file.getSize();
        String filename = UUID.randomUUID() + "." + extension;
        String cheminRelatif =  "/document/" + filename;
        String cheminPhysique = documentsStoragePath + cheminRelatif;

        try {
            Files.copy(file.getInputStream(), Paths.get(cheminPhysique));
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du fichier", e);
        }

        DocumentRequestDTO request = DocumentRequestDTO.builder()
                .nom(nom)
                .description(description)
                .extension(extension)
                .taille(taille)
                .chemin(cheminRelatif)
                .version(1)
                .typeId(typeId)
                .build();

        return documentService.createDocument(request);
    }

    @Override
    public Resource getDocumentFile(Long documentId, boolean checkIfReadable) {
        DocumentDTO document = documentService.findDocumentById(documentId);
        String resourcePath = "static" + document.getChemin();
        Resource resource = new ClassPathResource(resourcePath);

        if (!resource.exists()) {
            throw new RuntimeException("Fichier non trouvé");
        }
        if (checkIfReadable && !resource.isReadable()) {
            throw new RuntimeException("Fichier illisible");
        }

        return resource;
    }

    @Override
    public DocumentViewUrlDTO buildViewUrls(DocumentDTO document) {
        return DocumentViewUrlDTO.builder()
                .documentId(document.getId())
                .viewUrl(apiBasePath+"/documents/"  + document.getId() + "/view")
                .downloadUrl(apiBasePath+"/documents/" + document.getId() + "/view?download=true")
                .contentType(AppUtils.getContentTypeByExtension(document.getExtension()))
                .canPreview(AppUtils.isPreviewable(document.getExtension()))
                .build();
    }

    @Override
    public DocumentDebugInfo buildDebugInfo(DocumentDTO document, Resource resource) {
        String resourcePath = "static" + document.getChemin();

        long tailleReelle = -1;
        try {
            if (resource.exists()) {
                tailleReelle = resource.contentLength();
            }
        } catch (IOException ignored) {}

        return DocumentDebugInfo.builder()
                .documentId(document.getId())
                .nomDocument(document.getNom())
                .cheminStocke(document.getChemin())
                .cheminResolu(resourcePath)
                .fichierExiste(resource.exists())
                .fichierLisible(resource.isReadable())
                .tailleStockee(document.getTaille())
                .tailleReelle(tailleReelle)
                .repertoireBase("classpath:static")
                .build();
    }


}