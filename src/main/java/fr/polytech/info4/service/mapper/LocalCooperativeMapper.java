package fr.polytech.info4.service.mapper;

import fr.polytech.info4.domain.*;
import fr.polytech.info4.service.dto.LocalCooperativeDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LocalCooperative} and its DTO {@link LocalCooperativeDTO}.
 */
@Mapper(componentModel = "spring", uses = { NationalCooperativeMapper.class })
public interface LocalCooperativeMapper extends EntityMapper<LocalCooperativeDTO, LocalCooperative> {
    @Mapping(target = "nationalCooperative", source = "nationalCooperative", qualifiedByName = "id")
    LocalCooperativeDTO toDto(LocalCooperative s);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<LocalCooperativeDTO> toDtoIdSet(Set<LocalCooperative> localCooperative);
}
