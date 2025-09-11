package sn.afrilins.net.gestionEnquete.services.mapper.enquete;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import sn.afrilins.net.gestionEnquete.domain.enquete.SourceInfo;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.source_info.response.SourceInfoDTO;
import sn.afrilins.net.gestionEnquete.services.mapper.parametrage.DocumentMapper;
import sn.afrilins.net.gestionEnquete.services.mapper.parametrage.UtilisateurMapper;
import sn.afrilins.net.gestionEnquete.util.EntityMapper;



@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { DocumentMapper.class, UtilisateurMapper.class, EtatSourceInfoMapper.class, TypeSourceMapper.class }
)
public interface SourceInfoMapper extends EntityMapper<SourceInfoDTO, SourceInfo> {
}
