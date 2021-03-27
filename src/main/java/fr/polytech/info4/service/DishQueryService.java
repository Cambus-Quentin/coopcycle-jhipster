package fr.polytech.info4.service;

import fr.polytech.info4.domain.*; // for static metamodels
import fr.polytech.info4.domain.Dish;
import fr.polytech.info4.repository.DishRepository;
import fr.polytech.info4.service.criteria.DishCriteria;
import fr.polytech.info4.service.dto.DishDTO;
import fr.polytech.info4.service.mapper.DishMapper;
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
 * Service for executing complex queries for {@link Dish} entities in the database.
 * The main input is a {@link DishCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DishDTO} or a {@link Page} of {@link DishDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DishQueryService extends QueryService<Dish> {

    private final Logger log = LoggerFactory.getLogger(DishQueryService.class);

    private final DishRepository dishRepository;

    private final DishMapper dishMapper;

    public DishQueryService(DishRepository dishRepository, DishMapper dishMapper) {
        this.dishRepository = dishRepository;
        this.dishMapper = dishMapper;
    }

    /**
     * Return a {@link List} of {@link DishDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DishDTO> findByCriteria(DishCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Dish> specification = createSpecification(criteria);
        return dishMapper.toDto(dishRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DishDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DishDTO> findByCriteria(DishCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Dish> specification = createSpecification(criteria);
        return dishRepository.findAll(specification, page).map(dishMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DishCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Dish> specification = createSpecification(criteria);
        return dishRepository.count(specification);
    }

    /**
     * Function to convert {@link DishCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Dish> createSpecification(DishCriteria criteria) {
        Specification<Dish> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Dish_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Dish_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Dish_.price));
            }
            if (criteria.getRestaurantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRestaurantId(),
                            root -> root.join(Dish_.restaurant, JoinType.LEFT).get(Restaurant_.id)
                        )
                    );
            }
            if (criteria.getCommandId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCommandId(), root -> root.join(Dish_.commands, JoinType.LEFT).get(Command_.id))
                    );
            }
        }
        return specification;
    }
}
