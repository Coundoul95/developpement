package sn.afrilins.net.gestionEnquete.services.mapper.demande;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import sn.afrilins.net.gestionEnquete.domain.demande.Concerne;
import sn.afrilins.net.gestionEnquete.services.dto.demande.concerne.response.ConcerneDTO;
import sn.afrilins.net.gestionEnquete.util.EntityMapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConcerneMapper extends EntityMapper<ConcerneDTO, Concerne> {
}

//@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
//public interface ConcerneMapper extends EntityMapper<ConcerneDTO, Concerne> {
//    @Override
//    @Mapping(target = "type", expression = "java(mapStringToEnum(concerne.getType()))")
//    ConcerneDTO toDto(Concerne concerne);
//
//    @Override
//    @Mapping(target = "type", expression = "java(mapEnumToString(dto.getType()))")
//    Concerne toEntity(ConcerneDTO dto);
//
//    default TypeConcerne mapStringToEnum(String value) {
//        return value != null ? TypeConcerne.fromValue(value) : null;
//    }
//
//    default String mapEnumToString(TypeConcerne typeConcerne) {
//        return typeConcerne != null ? typeConcerne.toString() : null;
//    }
//}
