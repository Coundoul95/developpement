package sn.afrilins.net.gestionEnquete.services.mapper.demande;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import sn.afrilins.net.gestionEnquete.domain.demande.DemandeEnquete;
import sn.afrilins.net.gestionEnquete.services.dto.demande.demande_enquete.response.DemandeEnqueteDTO;
import sn.afrilins.net.gestionEnquete.services.mapper.enquete.EnqueteMapper;
import sn.afrilins.net.gestionEnquete.services.mapper.parametrage.DocumentMapper;
import sn.afrilins.net.gestionEnquete.services.mapper.parametrage.UtilisateurMapper;
import sn.afrilins.net.gestionEnquete.util.EntityMapper;


@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { UtilisateurMapper.class, EtatDemandeMapper.class, ConcerneMapper.class, EnqueteMapper.class, DocumentMapper.class}
)
public interface DemandeEnqueteMapper extends EntityMapper<DemandeEnqueteDTO, DemandeEnquete> {
}
