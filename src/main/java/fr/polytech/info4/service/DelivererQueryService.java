package fr.polytech.info4.service;

import fr.polytech.info4.domain.*; // for static metamodels
import fr.polytech.info4.domain.Deliverer;
import fr.polytech.info4.repository.DelivererRepository;
import fr.polytech.info4.service.criteria.DelivererCriteria;
import fr.polytech.info4.service.dto.DelivererDTO;
import fr.polytech.info4.service.mapper.DelivererMapper;
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
 * Service for executing complex queries for {@link Deliverer} entities in the database.
 * The main input is a {@link DelivererCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DelivererDTO} or a {@link Page} of {@link DelivererDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DelivererQueryService extends QueryService<Deliverer> {

    private final Logger log = LoggerFactory.getLogger(DelivererQueryService.class);

    private final DelivererRepository delivererRepository;

    private final DelivererMapper delivererMapper;

    public DelivererQueryService(DelivererRepository delivererRepository, DelivererMapper delivererMapper) {
        this.delivererRepository = delivererRepository;
        this.delivererMapper = delivererMapper;
    }

    /**
     * Return a {@link List} of {@link DelivererDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DelivererDTO> findByCriteria(DelivererCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Deliverer> specification = createSpecification(criteria);
        return delivererMapper.toDto(delivererRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DelivererDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DelivererDTO> findByCriteria(DelivererCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Deliverer> specification = createSpecification(criteria);
        return delivererRepository.findAll(specification, page).map(delivererMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DelivererCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Deliverer> specification = createSpecification(criteria);
        return delivererRepository.count(specification);
    }

    /**
     * Function to convert {@link DelivererCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Deliverer> createSpecification(DelivererCriteria criteria) {
        Specification<Deliverer> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Deliverer_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Deliverer_.name));
            }
            if (criteria.getFirstname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstname(), Deliverer_.firstname));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Deliverer_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getDeliveryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDeliveryId(),
                            root -> root.join(Deliverer_.deliveries, JoinType.LEFT).get(Delivery_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
