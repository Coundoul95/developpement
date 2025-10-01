package sn.afrilins.net.gestionEnquete.services.mapper.parametrage;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import sn.afrilins.net.gestionEnquete.domain.parametrage.TypeDocument;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.document.response.TypeDocumentDTO;
import sn.afrilins.net.gestionEnquete.util.EntityMapper;



@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TypeDocumentMapper extends EntityMapper<TypeDocumentDTO, TypeDocument> {
}
