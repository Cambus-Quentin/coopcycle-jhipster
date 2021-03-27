package fr.polytech.info4.service.mapper;

import fr.polytech.info4.domain.*;
import fr.polytech.info4.service.dto.DelivererDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Deliverer} and its DTO {@link DelivererDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface DelivererMapper extends EntityMapper<DelivererDTO, Deliverer> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    DelivererDTO toDto(Deliverer s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DelivererDTO toDtoId(Deliverer deliverer);
}
