package sn.afrilins.net.gestionEnquete.services.mapper.enquete;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import sn.afrilins.net.gestionEnquete.domain.enquete.EvenementCalendrier;
import sn.afrilins.net.gestionEnquete.services.dto.enquete.calendrier.response.EvenementCalendrierDTO;
import sn.afrilins.net.gestionEnquete.services.mapper.parametrage.UtilisateurMapper;
import sn.afrilins.net.gestionEnquete.util.EntityMapper;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { TypeEvenementMapper.class, UtilisateurMapper.class}
)
public interface EvenementCalendrierMapper extends EntityMapper<EvenementCalendrierDTO, EvenementCalendrier> {
}
