package sn.afrilins.net.gestionEnquete.services.mapper.enquete;


import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import sn.afrilins.net.gestionEnquete.domain.enquete.TypeEvenement;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.calendrier.response.TypeEvenementDTO;
import sn.afrilins.net.gestionEnquete.util.EntityMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TypeEvenementMapper extends EntityMapper<TypeEvenementDTO, TypeEvenement> {
}
