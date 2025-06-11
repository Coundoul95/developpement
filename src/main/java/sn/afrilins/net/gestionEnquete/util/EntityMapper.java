package sn.afrilins.net.gestionEnquete.util;

import org.mapstruct.Mapping;

public interface EntityMapper<D, E> {

    E toEntity(D dto);

    D toDto(E entity);

}
