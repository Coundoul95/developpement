package sn.afrilins.net.gestionEnquete.services.mapper.demande;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import sn.afrilins.net.gestionEnquete.domain.demande.EtatDemande;
import sn.afrilins.net.gestionEnquete.services.dto.demande.demande_enquete.response.EtatDemandeDTO;
import sn.afrilins.net.gestionEnquete.util.EntityMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface EtatDemandeMapper extends EntityMapper<EtatDemandeDTO, EtatDemande>{
}
