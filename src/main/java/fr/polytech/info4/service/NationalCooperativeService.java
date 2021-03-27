package fr.polytech.info4.service;

import fr.polytech.info4.service.dto.NationalCooperativeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.polytech.info4.domain.NationalCooperative}.
 */
public interface NationalCooperativeService {
    /**
     * Save a nationalCooperative.
     *
     * @param nationalCooperativeDTO the entity to save.
     * @return the persisted entity.
     */
    NationalCooperativeDTO save(NationalCooperativeDTO nationalCooperativeDTO);

    /**
     * Partially updates a nationalCooperative.
     *
     * @param nationalCooperativeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NationalCooperativeDTO> partialUpdate(NationalCooperativeDTO nationalCooperativeDTO);

    /**
     * Get all the nationalCooperatives.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NationalCooperativeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" nationalCooperative.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NationalCooperativeDTO> findOne(Long id);

    /**
     * Delete the "id" nationalCooperative.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
