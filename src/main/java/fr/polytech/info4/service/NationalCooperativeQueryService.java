package fr.polytech.info4.service;

import fr.polytech.info4.domain.*; // for static metamodels
import fr.polytech.info4.domain.NationalCooperative;
import fr.polytech.info4.repository.NationalCooperativeRepository;
import fr.polytech.info4.service.criteria.NationalCooperativeCriteria;
import fr.polytech.info4.service.dto.NationalCooperativeDTO;
import fr.polytech.info4.service.mapper.NationalCooperativeMapper;
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
 * Service for executing complex queries for {@link NationalCooperative} entities in the database.
 * The main input is a {@link NationalCooperativeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NationalCooperativeDTO} or a {@link Page} of {@link NationalCooperativeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NationalCooperativeQueryService extends QueryService<NationalCooperative> {

    private final Logger log = LoggerFactory.getLogger(NationalCooperativeQueryService.class);

    private final NationalCooperativeRepository nationalCooperativeRepository;

    private final NationalCooperativeMapper nationalCooperativeMapper;

    public NationalCooperativeQueryService(
        NationalCooperativeRepository nationalCooperativeRepository,
        NationalCooperativeMapper nationalCooperativeMapper
    ) {
        this.nationalCooperativeRepository = nationalCooperativeRepository;
        this.nationalCooperativeMapper = nationalCooperativeMapper;
    }

    /**
     * Return a {@link List} of {@link NationalCooperativeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NationalCooperativeDTO> findByCriteria(NationalCooperativeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<NationalCooperative> specification = createSpecification(criteria);
        return nationalCooperativeMapper.toDto(nationalCooperativeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NationalCooperativeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NationalCooperativeDTO> findByCriteria(NationalCooperativeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NationalCooperative> specification = createSpecification(criteria);
        return nationalCooperativeRepository.findAll(specification, page).map(nationalCooperativeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NationalCooperativeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<NationalCooperative> specification = createSpecification(criteria);
        return nationalCooperativeRepository.count(specification);
    }

    /**
     * Function to convert {@link NationalCooperativeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NationalCooperative> createSpecification(NationalCooperativeCriteria criteria) {
        Specification<NationalCooperative> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NationalCooperative_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NationalCooperative_.name));
            }
            if (criteria.getLocalCooperativeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLocalCooperativeId(),
                            root -> root.join(NationalCooperative_.localCooperatives, JoinType.LEFT).get(LocalCooperative_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
