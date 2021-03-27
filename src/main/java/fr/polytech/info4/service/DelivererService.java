package fr.polytech.info4.service;

import fr.polytech.info4.service.dto.DelivererDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.polytech.info4.domain.Deliverer}.
 */
public interface DelivererService {
    /**
     * Save a deliverer.
     *
     * @param delivererDTO the entity to save.
     * @return the persisted entity.
     */
    DelivererDTO save(DelivererDTO delivererDTO);

    /**
     * Partially updates a deliverer.
     *
     * @param delivererDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DelivererDTO> partialUpdate(DelivererDTO delivererDTO);

    /**
     * Get all the deliverers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DelivererDTO> findAll(Pageable pageable);

    /**
     * Get the "id" deliverer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DelivererDTO> findOne(Long id);

    /**
     * Delete the "id" deliverer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
