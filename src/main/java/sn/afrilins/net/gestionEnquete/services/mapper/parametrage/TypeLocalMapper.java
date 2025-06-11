package sn.afrilins.net.gestionEnquete.services.mapper.parametrage;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import sn.afrilins.net.gestionEnquete.domain.parametrage.TypeLocal;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.TypeLocalDTO;
import sn.afrilins.net.gestionEnquete.util.EntityMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TypeLocalMapper extends EntityMapper<TypeLocalDTO, TypeLocal> {
}
