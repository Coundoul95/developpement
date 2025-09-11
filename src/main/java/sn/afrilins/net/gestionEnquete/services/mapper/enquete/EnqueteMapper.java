package sn.afrilins.net.gestionEnquete.services.mapper.enquete;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import sn.afrilins.net.gestionEnquete.domain.enquete.Enquete;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.response.EnqueteDTO;
import sn.afrilins.net.gestionEnquete.services.mapper.parametrage.UtilisateurMapper;
import sn.afrilins.net.gestionEnquete.util.EntityMapper;


//@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { EtatEnqueteMapper.class, UtilisateurMapper.class}
)
public interface EnqueteMapper extends EntityMapper<EnqueteDTO, Enquete> {

}
