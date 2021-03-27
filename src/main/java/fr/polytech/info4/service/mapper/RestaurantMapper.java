package fr.polytech.info4.service.mapper;

import fr.polytech.info4.domain.*;
import fr.polytech.info4.service.dto.RestaurantDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Restaurant} and its DTO {@link RestaurantDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, LocalCooperativeMapper.class })
public interface RestaurantMapper extends EntityMapper<RestaurantDTO, Restaurant> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    @Mapping(target = "localCooperatives", source = "localCooperatives", qualifiedByName = "idSet")
    RestaurantDTO toDto(Restaurant s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RestaurantDTO toDtoId(Restaurant restaurant);

    @Mapping(target = "removeLocalCooperative", ignore = true)
    Restaurant toEntity(RestaurantDTO restaurantDTO);
}
