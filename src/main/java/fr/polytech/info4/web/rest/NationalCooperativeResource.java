package fr.polytech.info4.web.rest;

import fr.polytech.info4.repository.NationalCooperativeRepository;
import fr.polytech.info4.service.NationalCooperativeQueryService;
import fr.polytech.info4.service.NationalCooperativeService;
import fr.polytech.info4.service.criteria.NationalCooperativeCriteria;
import fr.polytech.info4.service.dto.NationalCooperativeDTO;
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
 * REST controller for managing {@link fr.polytech.info4.domain.NationalCooperative}.
 */
@RestController
@RequestMapping("/api")
public class NationalCooperativeResource {

    private final Logger log = LoggerFactory.getLogger(NationalCooperativeResource.class);

    private static final String ENTITY_NAME = "nationalCooperative";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NationalCooperativeService nationalCooperativeService;

    private final NationalCooperativeRepository nationalCooperativeRepository;

    private final NationalCooperativeQueryService nationalCooperativeQueryService;

    public NationalCooperativeResource(
        NationalCooperativeService nationalCooperativeService,
        NationalCooperativeRepository nationalCooperativeRepository,
        NationalCooperativeQueryService nationalCooperativeQueryService
    ) {
        this.nationalCooperativeService = nationalCooperativeService;
        this.nationalCooperativeRepository = nationalCooperativeRepository;
        this.nationalCooperativeQueryService = nationalCooperativeQueryService;
    }

    /**
     * {@code POST  /national-cooperatives} : Create a new nationalCooperative.
     *
     * @param nationalCooperativeDTO the nationalCooperativeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nationalCooperativeDTO, or with status {@code 400 (Bad Request)} if the nationalCooperative has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/national-cooperatives")
    public ResponseEntity<NationalCooperativeDTO> createNationalCooperative(
        @Valid @RequestBody NationalCooperativeDTO nationalCooperativeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save NationalCooperative : {}", nationalCooperativeDTO);
        if (nationalCooperativeDTO.getId() != null) {
            throw new BadRequestAlertException("A new nationalCooperative cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NationalCooperativeDTO result = nationalCooperativeService.save(nationalCooperativeDTO);
        return ResponseEntity
            .created(new URI("/api/national-cooperatives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /national-cooperatives/:id} : Updates an existing nationalCooperative.
     *
     * @param id the id of the nationalCooperativeDTO to save.
     * @param nationalCooperativeDTO the nationalCooperativeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nationalCooperativeDTO,
     * or with status {@code 400 (Bad Request)} if the nationalCooperativeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nationalCooperativeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/national-cooperatives/{id}")
    public ResponseEntity<NationalCooperativeDTO> updateNationalCooperative(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NationalCooperativeDTO nationalCooperativeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update NationalCooperative : {}, {}", id, nationalCooperativeDTO);
        if (nationalCooperativeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nationalCooperativeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nationalCooperativeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NationalCooperativeDTO result = nationalCooperativeService.save(nationalCooperativeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nationalCooperativeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /national-cooperatives/:id} : Partial updates given fields of an existing nationalCooperative, field will ignore if it is null
     *
     * @param id the id of the nationalCooperativeDTO to save.
     * @param nationalCooperativeDTO the nationalCooperativeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nationalCooperativeDTO,
     * or with status {@code 400 (Bad Request)} if the nationalCooperativeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nationalCooperativeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nationalCooperativeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/national-cooperatives/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<NationalCooperativeDTO> partialUpdateNationalCooperative(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NationalCooperativeDTO nationalCooperativeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update NationalCooperative partially : {}, {}", id, nationalCooperativeDTO);
        if (nationalCooperativeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nationalCooperativeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nationalCooperativeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NationalCooperativeDTO> result = nationalCooperativeService.partialUpdate(nationalCooperativeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nationalCooperativeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /national-cooperatives} : get all the nationalCooperatives.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nationalCooperatives in body.
     */
    @GetMapping("/national-cooperatives")
    public ResponseEntity<List<NationalCooperativeDTO>> getAllNationalCooperatives(
        NationalCooperativeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get NationalCooperatives by criteria: {}", criteria);
        Page<NationalCooperativeDTO> page = nationalCooperativeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /national-cooperatives/count} : count all the nationalCooperatives.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/national-cooperatives/count")
    public ResponseEntity<Long> countNationalCooperatives(NationalCooperativeCriteria criteria) {
        log.debug("REST request to count NationalCooperatives by criteria: {}", criteria);
        return ResponseEntity.ok().body(nationalCooperativeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /national-cooperatives/:id} : get the "id" nationalCooperative.
     *
     * @param id the id of the nationalCooperativeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nationalCooperativeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/national-cooperatives/{id}")
    public ResponseEntity<NationalCooperativeDTO> getNationalCooperative(@PathVariable Long id) {
        log.debug("REST request to get NationalCooperative : {}", id);
        Optional<NationalCooperativeDTO> nationalCooperativeDTO = nationalCooperativeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nationalCooperativeDTO);
    }

    /**
     * {@code DELETE  /national-cooperatives/:id} : delete the "id" nationalCooperative.
     *
     * @param id the id of the nationalCooperativeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/national-cooperatives/{id}")
    public ResponseEntity<Void> deleteNationalCooperative(@PathVariable Long id) {
        log.debug("REST request to delete NationalCooperative : {}", id);
        nationalCooperativeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
