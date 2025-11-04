package sn.afrilins.net.gestionEnquete.services.interfaces.parametrage;

import org.springframework.web.multipart.MultipartFile;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.document.request.DocumentUploadRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.document.response.DocumentDTO;

import java.util.List;

public interface DocumentAssociationService {
    List<DocumentDTO> uploadAndAssociateDocuments(
            Long enqueteId,
            Long demandeId,
            Long sourceId,
            List<DocumentUploadRequestDTO> metadonnees,
            MultipartFile[] documents
    );

    void deleteDocument(Long id, Long enqueteId, Long demandeId, Long sourceInfoId);
}
