package sn.afrilins.net.gestionEnquete.services.interfaces.enquete;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.TypeSourceDTO;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.request.TypeSourceRequestDTO;

public interface TypeSourceService {

    TypeSourceDTO createTypeSource(TypeSourceRequestDTO typeSource);

    TypeSourceDTO updateTypeSource(TypeSourceDTO typeSource);

    void deleteTypeSource(Long id);

    TypeSourceDTO findTypeSourceById(Long id);

    Page<TypeSourceDTO> readAllTypeSources(Pageable pageable);
}

