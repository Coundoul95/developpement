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
import sn.afrilins.net.gestionEnquete.repository.demande.DemandeEnqueteRepository;
import sn.afrilins.net.gestionEnquete.repository.enquete.EnqueteRepository;
import sn.afrilins.net.gestionEnquete.repository.enquete.EtatEnqueteRepository;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.EnqueteDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.EnqueteService;
import sn.afrilins.net.gestionEnquete.services.mapper.enquete.EnqueteMapper;

import java.util.Objects;

@AllArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Transactional
public class EnqueteServiceImpl implements EnqueteService {

    final EnqueteRepository enqueteRepository;
    final EtatEnqueteRepository etatEnqueteRepository;
    final DemandeEnqueteRepository demandeEnqueteRepository;
    final EnqueteMapper enqueteMapper;

    static final String ENTITY = "enquete";

    @Override
    public EnqueteDTO createEnquete(Long demandeId) {
        var demande = demandeEnqueteRepository.findById(demandeId).orElseThrow(()->
                new CustomBadRequestException(
                    new BadRequestAlertException("demande_id", ENTITY, "demande_inexistant")
                )
        );
        var etat = etatEnqueteRepository.findFirstByCode("00").orElse(null);
        if(Objects.isNull(etat)){
            etat = etatEnqueteRepository.save(EtatEnquete.builder().code("00").libelle("En attente").build());
        }
        var entity = Enquete.builder().etat(etat).demandeEnquete(demande).progression(0).build();
        return enqueteMapper.toDto(enqueteRepository.save(entity));
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
