package sn.afrilins.net.gestionEnquete.services.mapper.enquete;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import sn.afrilins.net.gestionEnquete.domain.enquete.EtatSourceInfo;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.source_info.response.EtatSourceInfoDTO;
import sn.afrilins.net.gestionEnquete.util.EntityMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface EtatSourceInfoMapper extends EntityMapper<EtatSourceInfoDTO, EtatSourceInfo> {
}
