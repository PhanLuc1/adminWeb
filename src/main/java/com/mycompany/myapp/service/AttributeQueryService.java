package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Attribute;
import com.mycompany.myapp.repository.AttributeRepository;
import com.mycompany.myapp.service.criteria.AttributeCriteria;
import com.mycompany.myapp.service.dto.AttributeDTO;
import com.mycompany.myapp.service.mapper.AttributeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Attribute} entities in the database.
 * The main input is a {@link AttributeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AttributeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AttributeQueryService extends QueryService<Attribute> {

    private static final Logger LOG = LoggerFactory.getLogger(AttributeQueryService.class);

    private final AttributeRepository attributeRepository;

    private final AttributeMapper attributeMapper;

    public AttributeQueryService(AttributeRepository attributeRepository, AttributeMapper attributeMapper) {
        this.attributeRepository = attributeRepository;
        this.attributeMapper = attributeMapper;
    }

    /**
     * Return a {@link Page} of {@link AttributeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AttributeDTO> findByCriteria(AttributeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Attribute> specification = createSpecification(criteria);
        return attributeRepository.findAll(specification, page).map(attributeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AttributeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Attribute> specification = createSpecification(criteria);
        return attributeRepository.count(specification);
    }

    /**
     * Function to convert {@link AttributeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Attribute> createSpecification(AttributeCriteria criteria) {
        Specification<Attribute> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Attribute_.id));
            }
            if (criteria.getCreateAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateAt(), Attribute_.createAt));
            }
            if (criteria.getUpdateAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdateAt(), Attribute_.updateAt));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Attribute_.name));
            }
        }
        return specification;
    }
}
