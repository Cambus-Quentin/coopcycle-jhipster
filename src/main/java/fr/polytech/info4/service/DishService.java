package fr.polytech.info4.service;

import fr.polytech.info4.service.dto.DishDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.polytech.info4.domain.Dish}.
 */
public interface DishService {
    /**
     * Save a dish.
     *
     * @param dishDTO the entity to save.
     * @return the persisted entity.
     */
    DishDTO save(DishDTO dishDTO);

    /**
     * Partially updates a dish.
     *
     * @param dishDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DishDTO> partialUpdate(DishDTO dishDTO);

    /**
     * Get all the dishes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DishDTO> findAll(Pageable pageable);

    /**
     * Get the "id" dish.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DishDTO> findOne(Long id);

    /**
     * Delete the "id" dish.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
