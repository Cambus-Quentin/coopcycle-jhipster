package fr.polytech.info4.service.mapper;

import fr.polytech.info4.domain.*;
import fr.polytech.info4.service.dto.DishDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dish} and its DTO {@link DishDTO}.
 */
@Mapper(componentModel = "spring", uses = { RestaurantMapper.class })
public interface DishMapper extends EntityMapper<DishDTO, Dish> {
    @Mapping(target = "restaurant", source = "restaurant", qualifiedByName = "id")
    DishDTO toDto(Dish s);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<DishDTO> toDtoIdSet(Set<Dish> dish);
}
