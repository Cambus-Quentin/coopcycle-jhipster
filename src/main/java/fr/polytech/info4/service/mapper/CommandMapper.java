package fr.polytech.info4.service.mapper;

import fr.polytech.info4.domain.*;
import fr.polytech.info4.service.dto.CommandDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Command} and its DTO {@link CommandDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClientMapper.class, DeliveryMapper.class, RestaurantMapper.class, DishMapper.class })
public interface CommandMapper extends EntityMapper<CommandDTO, Command> {
    @Mapping(target = "client", source = "client", qualifiedByName = "id")
    @Mapping(target = "delivery", source = "delivery", qualifiedByName = "id")
    @Mapping(target = "restaurant", source = "restaurant", qualifiedByName = "id")
    @Mapping(target = "dishes", source = "dishes", qualifiedByName = "idSet")
    CommandDTO toDto(Command s);

    @Mapping(target = "removeDish", ignore = true)
    Command toEntity(CommandDTO commandDTO);
}
