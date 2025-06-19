package sn.afrilins.net.gestionEnquete.services.interfaces.parametrage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.TypeDocumentDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.TypeLocalDTO;

public interface TypeDocumentService {


    TypeDocumentDTO createTypeDocument(TypeDocumentDTO typeDocument);

    TypeDocumentDTO updateTypeDocument(TypeDocumentDTO typeDocument);

    void deleteTypeDocument(Long id);

    TypeDocumentDTO findTypeDocumentById(Long id);

    Page<TypeDocumentDTO> readAllTypeDocument(Pageable pageable);

}
