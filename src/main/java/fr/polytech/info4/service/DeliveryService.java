package fr.polytech.info4.service;

import fr.polytech.info4.service.dto.DeliveryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.polytech.info4.domain.Delivery}.
 */
public interface DeliveryService {
    /**
     * Save a delivery.
     *
     * @param deliveryDTO the entity to save.
     * @return the persisted entity.
     */
    DeliveryDTO save(DeliveryDTO deliveryDTO);

    /**
     * Partially updates a delivery.
     *
     * @param deliveryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DeliveryDTO> partialUpdate(DeliveryDTO deliveryDTO);

    /**
     * Get all the deliveries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DeliveryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" delivery.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DeliveryDTO> findOne(Long id);

    /**
     * Delete the "id" delivery.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
