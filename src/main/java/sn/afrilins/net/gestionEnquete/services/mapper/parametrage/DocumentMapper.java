package sn.afrilins.net.gestionEnquete.services.mapper.parametrage;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Document;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.DocumentDTO;
import sn.afrilins.net.gestionEnquete.util.EntityMapper;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        uses = {TypeDocumentMapper.class}
)
public interface DocumentMapper extends EntityMapper<DocumentDTO, Document> {
}
