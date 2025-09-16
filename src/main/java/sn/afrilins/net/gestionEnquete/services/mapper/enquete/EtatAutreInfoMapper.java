package sn.afrilins.net.gestionEnquete.services.mapper.enquete;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import sn.afrilins.net.gestionEnquete.domain.enquete.EtatAutreInfo;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.autre_info.response.EtatAutreInfoDTO;
import sn.afrilins.net.gestionEnquete.util.EntityMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface EtatAutreInfoMapper extends EntityMapper<EtatAutreInfoDTO, EtatAutreInfo> {
}
