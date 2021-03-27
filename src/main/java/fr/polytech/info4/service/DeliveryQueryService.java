package fr.polytech.info4.service;

import fr.polytech.info4.domain.*; // for static metamodels
import fr.polytech.info4.domain.Delivery;
import fr.polytech.info4.repository.DeliveryRepository;
import fr.polytech.info4.service.criteria.DeliveryCriteria;
import fr.polytech.info4.service.dto.DeliveryDTO;
import fr.polytech.info4.service.mapper.DeliveryMapper;
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
 * Service for executing complex queries for {@link Delivery} entities in the database.
 * The main input is a {@link DeliveryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DeliveryDTO} or a {@link Page} of {@link DeliveryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DeliveryQueryService extends QueryService<Delivery> {

    private final Logger log = LoggerFactory.getLogger(DeliveryQueryService.class);

    private final DeliveryRepository deliveryRepository;

    private final DeliveryMapper deliveryMapper;

    public DeliveryQueryService(DeliveryRepository deliveryRepository, DeliveryMapper deliveryMapper) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryMapper = deliveryMapper;
    }

    /**
     * Return a {@link List} of {@link DeliveryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DeliveryDTO> findByCriteria(DeliveryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Delivery> specification = createSpecification(criteria);
        return deliveryMapper.toDto(deliveryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DeliveryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DeliveryDTO> findByCriteria(DeliveryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Delivery> specification = createSpecification(criteria);
        return deliveryRepository.findAll(specification, page).map(deliveryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DeliveryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Delivery> specification = createSpecification(criteria);
        return deliveryRepository.count(specification);
    }

    /**
     * Function to convert {@link DeliveryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Delivery> createSpecification(DeliveryCriteria criteria) {
        Specification<Delivery> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Delivery_.id));
            }
            if (criteria.getDeliveryAddr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDeliveryAddr(), Delivery_.deliveryAddr));
            }
            if (criteria.getDistance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDistance(), Delivery_.distance));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Delivery_.price));
            }
            if (criteria.getDelivererId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDelivererId(),
                            root -> root.join(Delivery_.deliverer, JoinType.LEFT).get(Deliverer_.id)
                        )
                    );
            }
            if (criteria.getCommandId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCommandId(), root -> root.join(Delivery_.commands, JoinType.LEFT).get(Command_.id))
                    );
            }
        }
        return specification;
    }
}
