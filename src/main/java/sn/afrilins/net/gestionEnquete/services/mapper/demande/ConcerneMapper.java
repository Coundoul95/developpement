package sn.afrilins.net.gestionEnquete.services.mapper.demande;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import sn.afrilins.net.gestionEnquete.domain.demande.Concerne;
import sn.afrilins.net.gestionEnquete.services.dto.demande.concerne.response.ConcerneDTO;
import sn.afrilins.net.gestionEnquete.util.EntityMapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConcerneMapper extends EntityMapper<ConcerneDTO, Concerne> {
}
