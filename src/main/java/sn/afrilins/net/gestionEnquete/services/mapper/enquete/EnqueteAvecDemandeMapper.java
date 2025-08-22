package sn.afrilins.net.gestionEnquete.services.mapper.enquete;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import sn.afrilins.net.gestionEnquete.domain.enquete.Enquete;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.EnqueteAvecDemandeDTO;
import sn.afrilins.net.gestionEnquete.services.mapper.demande.DemandeSansEnqueteMapper;
import sn.afrilins.net.gestionEnquete.util.EntityMapper;


@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { DemandeSansEnqueteMapper.class }
)
public interface EnqueteAvecDemandeMapper extends EntityMapper<EnqueteAvecDemandeDTO, Enquete>  {

    @Override
    @Mapping(source = "demandeEnquete", target = "demande")
    EnqueteAvecDemandeDTO toDto(Enquete entity);
}
