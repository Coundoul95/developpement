package sn.afrilins.net.gestionEnquete.services.mapper.parametrage;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import sn.afrilins.net.gestionEnquete.domain.parametrage.CentreDeGestion;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.CentreDeGestionDTO;
import sn.afrilins.net.gestionEnquete.util.EntityMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CentreDeGestionMapper extends EntityMapper<CentreDeGestionDTO, CentreDeGestion> {
}
