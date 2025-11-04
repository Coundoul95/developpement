package sn.afrilins.net.gestionEnquete.services.mapper.enquete;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import sn.afrilins.net.gestionEnquete.domain.enquete.AutreInfo;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.autre_info.response.AutreInfoDTO;
import sn.afrilins.net.gestionEnquete.services.mapper.demande.DemandeSansEnqueteMapper;
import sn.afrilins.net.gestionEnquete.util.EntityMapper;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {EtatAutreInfoMapper.class}
)
public interface AutreInfoMapper extends EntityMapper<AutreInfoDTO, AutreInfo> {
}
