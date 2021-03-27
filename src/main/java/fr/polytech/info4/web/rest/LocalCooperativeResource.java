package fr.polytech.info4.web.rest;

import fr.polytech.info4.repository.LocalCooperativeRepository;
import fr.polytech.info4.service.LocalCooperativeQueryService;
import fr.polytech.info4.service.LocalCooperativeService;
import fr.polytech.info4.service.criteria.LocalCooperativeCriteria;
import fr.polytech.info4.service.dto.LocalCooperativeDTO;
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
 * REST controller for managing {@link fr.polytech.info4.domain.LocalCooperative}.
 */
@RestController
@RequestMapping("/api")
public class LocalCooperativeResource {

    private final Logger log = LoggerFactory.getLogger(LocalCooperativeResource.class);

    private static final String ENTITY_NAME = "localCooperative";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocalCooperativeService localCooperativeService;

    private final LocalCooperativeRepository localCooperativeRepository;

    private final LocalCooperativeQueryService localCooperativeQueryService;

    public LocalCooperativeResource(
        LocalCooperativeService localCooperativeService,
        LocalCooperativeRepository localCooperativeRepository,
        LocalCooperativeQueryService localCooperativeQueryService
    ) {
        this.localCooperativeService = localCooperativeService;
        this.localCooperativeRepository = localCooperativeRepository;
        this.localCooperativeQueryService = localCooperativeQueryService;
    }

    /**
     * {@code POST  /local-cooperatives} : Create a new localCooperative.
     *
     * @param localCooperativeDTO the localCooperativeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new localCooperativeDTO, or with status {@code 400 (Bad Request)} if the localCooperative has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/local-cooperatives")
    public ResponseEntity<LocalCooperativeDTO> createLocalCooperative(@Valid @RequestBody LocalCooperativeDTO localCooperativeDTO)
        throws URISyntaxException {
        log.debug("REST request to save LocalCooperative : {}", localCooperativeDTO);
        if (localCooperativeDTO.getId() != null) {
            throw new BadRequestAlertException("A new localCooperative cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocalCooperativeDTO result = localCooperativeService.save(localCooperativeDTO);
        return ResponseEntity
            .created(new URI("/api/local-cooperatives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /local-cooperatives/:id} : Updates an existing localCooperative.
     *
     * @param id the id of the localCooperativeDTO to save.
     * @param localCooperativeDTO the localCooperativeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated localCooperativeDTO,
     * or with status {@code 400 (Bad Request)} if the localCooperativeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the localCooperativeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/local-cooperatives/{id}")
    public ResponseEntity<LocalCooperativeDTO> updateLocalCooperative(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LocalCooperativeDTO localCooperativeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LocalCooperative : {}, {}", id, localCooperativeDTO);
        if (localCooperativeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, localCooperativeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!localCooperativeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LocalCooperativeDTO result = localCooperativeService.save(localCooperativeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, localCooperativeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /local-cooperatives/:id} : Partial updates given fields of an existing localCooperative, field will ignore if it is null
     *
     * @param id the id of the localCooperativeDTO to save.
     * @param localCooperativeDTO the localCooperativeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated localCooperativeDTO,
     * or with status {@code 400 (Bad Request)} if the localCooperativeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the localCooperativeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the localCooperativeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/local-cooperatives/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LocalCooperativeDTO> partialUpdateLocalCooperative(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LocalCooperativeDTO localCooperativeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LocalCooperative partially : {}, {}", id, localCooperativeDTO);
        if (localCooperativeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, localCooperativeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!localCooperativeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LocalCooperativeDTO> result = localCooperativeService.partialUpdate(localCooperativeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, localCooperativeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /local-cooperatives} : get all the localCooperatives.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of localCooperatives in body.
     */
    @GetMapping("/local-cooperatives")
    public ResponseEntity<List<LocalCooperativeDTO>> getAllLocalCooperatives(LocalCooperativeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LocalCooperatives by criteria: {}", criteria);
        Page<LocalCooperativeDTO> page = localCooperativeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /local-cooperatives/count} : count all the localCooperatives.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/local-cooperatives/count")
    public ResponseEntity<Long> countLocalCooperatives(LocalCooperativeCriteria criteria) {
        log.debug("REST request to count LocalCooperatives by criteria: {}", criteria);
        return ResponseEntity.ok().body(localCooperativeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /local-cooperatives/:id} : get the "id" localCooperative.
     *
     * @param id the id of the localCooperativeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the localCooperativeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/local-cooperatives/{id}")
    public ResponseEntity<LocalCooperativeDTO> getLocalCooperative(@PathVariable Long id) {
        log.debug("REST request to get LocalCooperative : {}", id);
        Optional<LocalCooperativeDTO> localCooperativeDTO = localCooperativeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(localCooperativeDTO);
    }

    /**
     * {@code DELETE  /local-cooperatives/:id} : delete the "id" localCooperative.
     *
     * @param id the id of the localCooperativeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/local-cooperatives/{id}")
    public ResponseEntity<Void> deleteLocalCooperative(@PathVariable Long id) {
        log.debug("REST request to delete LocalCooperative : {}", id);
        localCooperativeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
