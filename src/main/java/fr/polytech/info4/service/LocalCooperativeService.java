package fr.polytech.info4.service;

import fr.polytech.info4.service.dto.LocalCooperativeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.polytech.info4.domain.LocalCooperative}.
 */
public interface LocalCooperativeService {
    /**
     * Save a localCooperative.
     *
     * @param localCooperativeDTO the entity to save.
     * @return the persisted entity.
     */
    LocalCooperativeDTO save(LocalCooperativeDTO localCooperativeDTO);

    /**
     * Partially updates a localCooperative.
     *
     * @param localCooperativeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LocalCooperativeDTO> partialUpdate(LocalCooperativeDTO localCooperativeDTO);

    /**
     * Get all the localCooperatives.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LocalCooperativeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" localCooperative.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LocalCooperativeDTO> findOne(Long id);

    /**
     * Delete the "id" localCooperative.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
