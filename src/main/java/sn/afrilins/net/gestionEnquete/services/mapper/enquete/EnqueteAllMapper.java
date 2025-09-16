package sn.afrilins.net.gestionEnquete.services.mapper.enquete;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import sn.afrilins.net.gestionEnquete.domain.enquete.Enquete;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.enquete.response.EnqueteAllDTO;
import sn.afrilins.net.gestionEnquete.services.mapper.demande.DemandeSansEnqueteMapper;
import sn.afrilins.net.gestionEnquete.services.mapper.parametrage.DocumentMapper;
import sn.afrilins.net.gestionEnquete.util.EntityMapper;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {DemandeSansEnqueteMapper.class, SourceInfoMapper.class, AutreInfoMapper.class, ConclusionMapper.class, DocumentMapper.class}
)
public interface EnqueteAllMapper extends EntityMapper<EnqueteAllDTO, Enquete> {

    @Override
    @Mapping(source = "demandeEnquete", target = "demande")
    EnqueteAllDTO toDto(Enquete entity);
}
