package fr.polytech.info4.service;

import fr.polytech.info4.domain.*; // for static metamodels
import fr.polytech.info4.domain.LocalCooperative;
import fr.polytech.info4.repository.LocalCooperativeRepository;
import fr.polytech.info4.service.criteria.LocalCooperativeCriteria;
import fr.polytech.info4.service.dto.LocalCooperativeDTO;
import fr.polytech.info4.service.mapper.LocalCooperativeMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link LocalCooperative} entities in the database.
 * The main input is a {@link LocalCooperativeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LocalCooperativeDTO} or a {@link Page} of {@link LocalCooperativeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LocalCooperativeQueryService extends QueryService<LocalCooperative> {

    private final Logger log = LoggerFactory.getLogger(LocalCooperativeQueryService.class);

    private final LocalCooperativeRepository localCooperativeRepository;

    private final LocalCooperativeMapper localCooperativeMapper;

    public LocalCooperativeQueryService(
        LocalCooperativeRepository localCooperativeRepository,
        LocalCooperativeMapper localCooperativeMapper
    ) {
        this.localCooperativeRepository = localCooperativeRepository;
        this.localCooperativeMapper = localCooperativeMapper;
    }

    /**
     * Return a {@link List} of {@link LocalCooperativeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LocalCooperativeDTO> findByCriteria(LocalCooperativeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LocalCooperative> specification = createSpecification(criteria);
        return localCooperativeMapper.toDto(localCooperativeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LocalCooperativeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LocalCooperativeDTO> findByCriteria(LocalCooperativeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LocalCooperative> specification = createSpecification(criteria);
        return localCooperativeRepository.findAll(specification, page).map(localCooperativeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LocalCooperativeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LocalCooperative> specification = createSpecification(criteria);
        return localCooperativeRepository.count(specification);
    }

    /**
     * Function to convert {@link LocalCooperativeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LocalCooperative> createSpecification(LocalCooperativeCriteria criteria) {
        Specification<LocalCooperative> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LocalCooperative_.id));
            }
            if (criteria.getGeoZone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGeoZone(), LocalCooperative_.geoZone));
            }
            if (criteria.getNationalCooperativeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNationalCooperativeId(),
                            root -> root.join(LocalCooperative_.nationalCooperative, JoinType.LEFT).get(NationalCooperative_.id)
                        )
                    );
            }
            if (criteria.getRestaurantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRestaurantId(),
                            root -> root.join(LocalCooperative_.restaurants, JoinType.LEFT).get(Restaurant_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
