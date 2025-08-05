package sn.afrilins.net.gestionEnquete.services.implement.enquete;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.afrilins.net.gestionEnquete.domain.enquete.Enquete;
import sn.afrilins.net.gestionEnquete.domain.enquete.EtatEnquete;
import sn.afrilins.net.gestionEnquete.exception.BadRequestAlertException;
import sn.afrilins.net.gestionEnquete.exception.CustomBadRequestException;
import sn.afrilins.net.gestionEnquete.repository.enquete.EnqueteRepository;
import sn.afrilins.net.gestionEnquete.repository.enquete.EtatEnqueteRepository;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.EnqueteDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.EnqueteService;

@AllArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Transactional
public class EnqueteServiceImpl implements EnqueteService {

    final EnqueteRepository enqueteRepository;
    final EtatEnqueteRepository etatEnqueteRepository;

    static final String ENTITY = "enquete";

    @Override
    public EnqueteDTO createEnquete(EnqueteDTO dto) {
//       var entity = Enquete.builder().etat(getEtatOrThrow(dto.)).dateDebut(dto.getDateDebut()).dateFin(dto.getDateFin()).build();
        return null;
    }

    @Override
    public EnqueteDTO updateEnquete(EnqueteDTO dto) {
        return null;
    }

    @Override
    public void deleteEnquete(Long id) {

    }

    @Override
    public EnqueteDTO findEnqueteById(Long id) {
        return null;
    }

    @Override
    public Page<EnqueteDTO> readAllEnquete(Pageable pageable) {
        return null;
    }


    private EtatEnquete getEtatOrThrow(String code) {
        return etatEnqueteRepository.findFirstByCode(code)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("etat_introuvable", ENTITY, "etat_inexistant")));
    }
}
