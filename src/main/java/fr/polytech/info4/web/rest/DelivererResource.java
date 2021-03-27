package fr.polytech.info4.web.rest;

import fr.polytech.info4.repository.DelivererRepository;
import fr.polytech.info4.service.DelivererQueryService;
import fr.polytech.info4.service.DelivererService;
import fr.polytech.info4.service.criteria.DelivererCriteria;
import fr.polytech.info4.service.dto.DelivererDTO;
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
 * REST controller for managing {@link fr.polytech.info4.domain.Deliverer}.
 */
@RestController
@RequestMapping("/api")
public class DelivererResource {

    private final Logger log = LoggerFactory.getLogger(DelivererResource.class);

    private static final String ENTITY_NAME = "deliverer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DelivererService delivererService;

    private final DelivererRepository delivererRepository;

    private final DelivererQueryService delivererQueryService;

    public DelivererResource(
        DelivererService delivererService,
        DelivererRepository delivererRepository,
        DelivererQueryService delivererQueryService
    ) {
        this.delivererService = delivererService;
        this.delivererRepository = delivererRepository;
        this.delivererQueryService = delivererQueryService;
    }

    /**
     * {@code POST  /deliverers} : Create a new deliverer.
     *
     * @param delivererDTO the delivererDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new delivererDTO, or with status {@code 400 (Bad Request)} if the deliverer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deliverers")
    public ResponseEntity<DelivererDTO> createDeliverer(@Valid @RequestBody DelivererDTO delivererDTO) throws URISyntaxException {
        log.debug("REST request to save Deliverer : {}", delivererDTO);
        if (delivererDTO.getId() != null) {
            throw new BadRequestAlertException("A new deliverer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DelivererDTO result = delivererService.save(delivererDTO);
        return ResponseEntity
            .created(new URI("/api/deliverers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /deliverers/:id} : Updates an existing deliverer.
     *
     * @param id the id of the delivererDTO to save.
     * @param delivererDTO the delivererDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated delivererDTO,
     * or with status {@code 400 (Bad Request)} if the delivererDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the delivererDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/deliverers/{id}")
    public ResponseEntity<DelivererDTO> updateDeliverer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DelivererDTO delivererDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Deliverer : {}, {}", id, delivererDTO);
        if (delivererDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, delivererDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!delivererRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DelivererDTO result = delivererService.save(delivererDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, delivererDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /deliverers/:id} : Partial updates given fields of an existing deliverer, field will ignore if it is null
     *
     * @param id the id of the delivererDTO to save.
     * @param delivererDTO the delivererDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated delivererDTO,
     * or with status {@code 400 (Bad Request)} if the delivererDTO is not valid,
     * or with status {@code 404 (Not Found)} if the delivererDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the delivererDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/deliverers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DelivererDTO> partialUpdateDeliverer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DelivererDTO delivererDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Deliverer partially : {}, {}", id, delivererDTO);
        if (delivererDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, delivererDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!delivererRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DelivererDTO> result = delivererService.partialUpdate(delivererDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, delivererDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /deliverers} : get all the deliverers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deliverers in body.
     */
    @GetMapping("/deliverers")
    public ResponseEntity<List<DelivererDTO>> getAllDeliverers(DelivererCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Deliverers by criteria: {}", criteria);
        Page<DelivererDTO> page = delivererQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /deliverers/count} : count all the deliverers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/deliverers/count")
    public ResponseEntity<Long> countDeliverers(DelivererCriteria criteria) {
        log.debug("REST request to count Deliverers by criteria: {}", criteria);
        return ResponseEntity.ok().body(delivererQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /deliverers/:id} : get the "id" deliverer.
     *
     * @param id the id of the delivererDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the delivererDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/deliverers/{id}")
    public ResponseEntity<DelivererDTO> getDeliverer(@PathVariable Long id) {
        log.debug("REST request to get Deliverer : {}", id);
        Optional<DelivererDTO> delivererDTO = delivererService.findOne(id);
        return ResponseUtil.wrapOrNotFound(delivererDTO);
    }

    /**
     * {@code DELETE  /deliverers/:id} : delete the "id" deliverer.
     *
     * @param id the id of the delivererDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deliverers/{id}")
    public ResponseEntity<Void> deleteDeliverer(@PathVariable Long id) {
        log.debug("REST request to delete Deliverer : {}", id);
        delivererService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
