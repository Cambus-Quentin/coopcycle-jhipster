package fr.polytech.info4.service.mapper;

import fr.polytech.info4.domain.*;
import fr.polytech.info4.service.dto.NationalCooperativeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NationalCooperative} and its DTO {@link NationalCooperativeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NationalCooperativeMapper extends EntityMapper<NationalCooperativeDTO, NationalCooperative> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NationalCooperativeDTO toDtoId(NationalCooperative nationalCooperative);
}
