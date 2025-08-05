package sn.afrilins.net.gestionEnquete.services.implement.demande;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.afrilins.net.gestionEnquete.domain.demande.Concerne;
import sn.afrilins.net.gestionEnquete.domain.demande.DemandeEnquete;
import sn.afrilins.net.gestionEnquete.domain.demande.EtatDemande;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Utilisateur;
import sn.afrilins.net.gestionEnquete.exception.BadRequestAlertException;
import sn.afrilins.net.gestionEnquete.exception.CustomBadRequestException;
import sn.afrilins.net.gestionEnquete.repository.demande.ConcerneRepository;
import sn.afrilins.net.gestionEnquete.repository.demande.DemandeEnqueteRepository;
import sn.afrilins.net.gestionEnquete.repository.demande.EtatDemandeRepository;
import sn.afrilins.net.gestionEnquete.repository.parametrage.UtilisateurRepository;
import sn.afrilins.net.gestionEnquete.services.dto.demande.DemandeEnqueteDTO;
import sn.afrilins.net.gestionEnquete.services.dto.demande.request.DemandeEnqueteRequestDTO;
import sn.afrilins.net.gestionEnquete.services.dto.demande.request.DemandeEnqueteUpdateRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.demande.DemandeEnqueteService;
import sn.afrilins.net.gestionEnquete.services.interfaces.enquete.EnqueteService;
import sn.afrilins.net.gestionEnquete.services.mapper.demande.DemandeEnqueteMapper;
import sn.afrilins.net.gestionEnquete.util.ValidationUtils;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Transactional
public class DemandeEnqueteServiceImpl implements DemandeEnqueteService {

    final DemandeEnqueteRepository demandeEnqueteRepository;
    final EtatDemandeRepository etatDemandeRepository;
    final ConcerneRepository concerneRepository;
    final UtilisateurRepository utilisateurRepository;
    final DemandeEnqueteMapper demandeEnqueteMapper;
    final EnqueteService enqueteService;

    static final String ENTITY = "demande_enquete";

    @Override
    public DemandeEnqueteDTO createDemandeEnquete(DemandeEnqueteRequestDTO dto) {
        validateDemande(dto);

        var concerne = getConcerneOrThrow(dto.getConcerneId());
        var utilisateur = getUtilisateurOrThrow(dto.getUtilisateurId());
        var etat = etatDemandeRepository.findFirstByCode("00")
                .orElseGet(() -> etatDemandeRepository.save(EtatDemande.builder().code("00").libelle("En attente").build()));

        var entity = DemandeEnquete.builder()
                .description(dto.getDescription())
                .etat(etat)
                .urgent(dto.getUrgent())
                .dateEcheance(dto.getDateEcheance())
                .utilisateur(utilisateur)
                .objet(dto.getObjet())
                .concerne(concerne)
                .build();

        return demandeEnqueteMapper.toDto(demandeEnqueteRepository.save(entity));
    }

    @Override
    public DemandeEnqueteDTO updateDemandeEnquete(Long id, DemandeEnqueteUpdateRequestDTO dto) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);
        var entity = getDemandeOrThrow(id);

        dto.getObjet().ifPresent(obj -> {
            ValidationUtils.requireNonBlank(obj, "objet", ENTITY);
            ValidationUtils.requireMinLength(obj, 3, "objet", ENTITY);
            entity.setObjet(obj);
        });

        dto.getPriorite().ifPresent(priorite -> {
            ValidationUtils.requireInRange(priorite, 1, 5, "priorite", ENTITY);
            entity.setPriorite(priorite);
        });

        dto.getUrgent().ifPresent(entity::setUrgent);

        dto.getCommentaireValidation().ifPresent(commentaire -> {
            ValidationUtils.requireNonBlank(commentaire, "commentaireValidation", ENTITY);
            entity.setCommentaireValidation(commentaire);
        });

        dto.getConcerneId().ifPresent(concerneId -> {
            ValidationUtils.requirePositiveId(concerneId, "concerneId", ENTITY);
            entity.setConcerne(getConcerneOrThrow(concerneId));
        });

        dto.getEtatCode().ifPresent(etatCode->{
            ValidationUtils.requireNonBlank(etatCode, "etatCode", ENTITY);
            entity.setDateAnnulation(null);
            entity.setDateValidation(null);
            if(etatCode == "01"){
                entity.setDateValidation(LocalDateTime.now());

            }
            if(etatCode == "02"){
                entity.setDateAnnulation(LocalDateTime.now());
            }
            entity.setEtat(getEtatOrThrow(etatCode));
        });

        return demandeEnqueteMapper.toDto(demandeEnqueteRepository.save(entity));
    }

    @Override
    public void deleteDemandeEnquete(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);
        var entity = getDemandeOrThrow(id);
        demandeEnqueteRepository.delete(entity);
    }

    @Override
    public DemandeEnqueteDTO findDemandeEnqueteById(Long id) {
        ValidationUtils.requirePositiveId(id, "id", ENTITY);
        return demandeEnqueteMapper.toDto(getDemandeOrThrow(id));
    }

    @Override
    public Page<DemandeEnqueteDTO> findAllDemandeEnquete(Pageable pageable) {
        return demandeEnqueteRepository.findAll(pageable).map(demandeEnqueteMapper::toDto);
    }

    @Override
    public DemandeEnqueteDTO changerEtatDemandeEnquete(Long demandeId, String nouvelEtatCode) {
        ValidationUtils.requirePositiveId(demandeId, "demande_id", ENTITY);
        ValidationUtils.requireNonBlank(nouvelEtatCode, "etat", ENTITY);
        ValidationUtils.requireMinLength(nouvelEtatCode, 2, "etat", ENTITY);

        var demande = getDemandeOrThrow(demandeId);
        var etat = etatDemandeRepository.findFirstByCode(nouvelEtatCode)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("etat_introuvable", ENTITY, "etat_inexistant")));

        demande.setEtat(etat);
        return demandeEnqueteMapper.toDto(demandeEnqueteRepository.save(demande));
    }

    private void validateDemande(DemandeEnqueteRequestDTO dto) {
        ValidationUtils.requireNonBlank(dto.getObjet(), "objet", ENTITY);
        ValidationUtils.requireMinLength(dto.getObjet(), 3, "objet", ENTITY);
        ValidationUtils.requireInRange(dto.getPriorite(), 1, 5, "priorite", ENTITY);
        ValidationUtils.requireNonNull(dto.getDateEcheance(), "date_echeance", ENTITY);
        ValidationUtils.requireNonNull(dto.getConcerneId(), "concerne_id", ENTITY);
        ValidationUtils.requireNonNull(dto.getUtilisateurId(), "utilisateur_id", ENTITY);
    }


    private DemandeEnquete getDemandeOrThrow(Long id) {
        return demandeEnqueteRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("demande_enquete_introuvable", ENTITY, "id_inexistant")));
    }

    private EtatDemande getEtatOrThrow(String code) {
        return etatDemandeRepository.findFirstByCode(code)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("etat_introuvable", ENTITY, "etat_inexistant")));
    }

    private Utilisateur getUtilisateurOrThrow(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("utilisateur_introuvable", ENTITY, "utilisateurId_invalide")));
    }

    private Concerne getConcerneOrThrow(Long id) {
        return concerneRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException(
                        new BadRequestAlertException("concerne_introuvable", ENTITY, "concerneId_invalide")));
    }
}
