package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.AttributeValue;
import com.mycompany.myapp.repository.AttributeValueRepository;
import com.mycompany.myapp.service.criteria.AttributeValueCriteria;
import com.mycompany.myapp.service.dto.AttributeValueDTO;
import com.mycompany.myapp.service.mapper.AttributeValueMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AttributeValue} entities in the database.
 * The main input is a {@link AttributeValueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AttributeValueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AttributeValueQueryService extends QueryService<AttributeValue> {

    private static final Logger LOG = LoggerFactory.getLogger(AttributeValueQueryService.class);

    private final AttributeValueRepository attributeValueRepository;

    private final AttributeValueMapper attributeValueMapper;

    public AttributeValueQueryService(AttributeValueRepository attributeValueRepository, AttributeValueMapper attributeValueMapper) {
        this.attributeValueRepository = attributeValueRepository;
        this.attributeValueMapper = attributeValueMapper;
    }

    /**
     * Return a {@link Page} of {@link AttributeValueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AttributeValueDTO> findByCriteria(AttributeValueCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AttributeValue> specification = createSpecification(criteria);
        return attributeValueRepository.findAll(specification, page).map(attributeValueMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AttributeValueCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AttributeValue> specification = createSpecification(criteria);
        return attributeValueRepository.count(specification);
    }

    /**
     * Function to convert {@link AttributeValueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AttributeValue> createSpecification(AttributeValueCriteria criteria) {
        Specification<AttributeValue> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AttributeValue_.id));
            }
            if (criteria.getCreatAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatAt(), AttributeValue_.creatAt));
            }
            if (criteria.getUpdateAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdateAt(), AttributeValue_.updateAt));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AttributeValue_.name));
            }
            if (criteria.getAttributeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAttributeId(), root ->
                        root.join(AttributeValue_.attribute, JoinType.LEFT).get(Attribute_.id)
                    )
                );
            }
            if (criteria.getProductVariantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductVariantId(), root ->
                        root.join(AttributeValue_.productVariants, JoinType.LEFT).get(ProductVariant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
