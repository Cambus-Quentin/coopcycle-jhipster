package fr.polytech.info4.web.rest;

import fr.polytech.info4.repository.DishRepository;
import fr.polytech.info4.service.DishQueryService;
import fr.polytech.info4.service.DishService;
import fr.polytech.info4.service.criteria.DishCriteria;
import fr.polytech.info4.service.dto.DishDTO;
import fr.polytech.info4.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.polytech.info4.domain.Dish}.
 */
@RestController
@RequestMapping("/api")
public class DishResource {

    private final Logger log = LoggerFactory.getLogger(DishResource.class);

    private static final String ENTITY_NAME = "dish";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DishService dishService;

    private final DishRepository dishRepository;

    private final DishQueryService dishQueryService;

    public DishResource(DishService dishService, DishRepository dishRepository, DishQueryService dishQueryService) {
        this.dishService = dishService;
        this.dishRepository = dishRepository;
        this.dishQueryService = dishQueryService;
    }

    /**
     * {@code POST  /dishes} : Create a new dish.
     *
     * @param dishDTO the dishDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dishDTO, or with status {@code 400 (Bad Request)} if the dish has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dishes")
    public ResponseEntity<DishDTO> createDish(@Valid @RequestBody DishDTO dishDTO) throws URISyntaxException {
        log.debug("REST request to save Dish : {}", dishDTO);
        if (dishDTO.getId() != null) {
            throw new BadRequestAlertException("A new dish cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DishDTO result = dishService.save(dishDTO);
        return ResponseEntity
            .created(new URI("/api/dishes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dishes/:id} : Updates an existing dish.
     *
     * @param id the id of the dishDTO to save.
     * @param dishDTO the dishDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dishDTO,
     * or with status {@code 400 (Bad Request)} if the dishDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dishDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dishes/{id}")
    public ResponseEntity<DishDTO> updateDish(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DishDTO dishDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Dish : {}, {}", id, dishDTO);
        if (dishDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dishDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dishRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DishDTO result = dishService.save(dishDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dishDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dishes/:id} : Partial updates given fields of an existing dish, field will ignore if it is null
     *
     * @param id the id of the dishDTO to save.
     * @param dishDTO the dishDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dishDTO,
     * or with status {@code 400 (Bad Request)} if the dishDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dishDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dishDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dishes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DishDTO> partialUpdateDish(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DishDTO dishDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dish partially : {}, {}", id, dishDTO);
        if (dishDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dishDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dishRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DishDTO> result = dishService.partialUpdate(dishDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dishDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dishes} : get all the dishes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dishes in body.
     */
    @GetMapping("/dishes")
    public ResponseEntity<List<DishDTO>> getAllDishes(DishCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Dishes by criteria: {}", criteria);
        Page<DishDTO> page = dishQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dishes/count} : count all the dishes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/dishes/count")
    public ResponseEntity<Long> countDishes(DishCriteria criteria) {
        log.debug("REST request to count Dishes by criteria: {}", criteria);
        return ResponseEntity.ok().body(dishQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /dishes/:id} : get the "id" dish.
     *
     * @param id the id of the dishDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dishDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dishes/{id}")
    public ResponseEntity<DishDTO> getDish(@PathVariable Long id) {
        log.debug("REST request to get Dish : {}", id);
        Optional<DishDTO> dishDTO = dishService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dishDTO);
    }

    /**
     * {@code DELETE  /dishes/:id} : delete the "id" dish.
     *
     * @param id the id of the dishDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dishes/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        log.debug("REST request to delete Dish : {}", id);
        dishService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
