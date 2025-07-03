package sn.afrilins.net.gestionEnquete.services.mapper.enquete;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import sn.afrilins.net.gestionEnquete.domain.enquete.Conclusion;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.ConclusionDTO;
import sn.afrilins.net.gestionEnquete.util.EntityMapper;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ConclusionMapper extends EntityMapper<ConclusionDTO, Conclusion> {
}
