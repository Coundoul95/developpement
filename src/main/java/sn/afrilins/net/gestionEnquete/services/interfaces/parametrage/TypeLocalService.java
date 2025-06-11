package sn.afrilins.net.gestionEnquete.services.interfaces.parametrage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.TypeLocalDTO;

public interface TypeLocalService {

    TypeLocalDTO createTypeLocal(TypeLocalDTO typeLocal);

    TypeLocalDTO updateTypeLocal(TypeLocalDTO typeLocal);

    void deleteTypeLocal(Long id);

    TypeLocalDTO findTypeLocalById(Long id);

    Page<TypeLocalDTO> readAllTypeLocal(Pageable pageable, String code, String libelle);

}
