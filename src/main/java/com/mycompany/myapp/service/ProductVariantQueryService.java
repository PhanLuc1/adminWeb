package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.ProductVariant;
import com.mycompany.myapp.repository.ProductVariantRepository;
import com.mycompany.myapp.service.criteria.ProductVariantCriteria;
import com.mycompany.myapp.service.dto.ProductVariantDTO;
import com.mycompany.myapp.service.mapper.ProductVariantMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ProductVariant} entities in the database.
 * The main input is a {@link ProductVariantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductVariantDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductVariantQueryService extends QueryService<ProductVariant> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductVariantQueryService.class);

    private final ProductVariantRepository productVariantRepository;

    private final ProductVariantMapper productVariantMapper;

    public ProductVariantQueryService(ProductVariantRepository productVariantRepository, ProductVariantMapper productVariantMapper) {
        this.productVariantRepository = productVariantRepository;
        this.productVariantMapper = productVariantMapper;
    }

    /**
     * Return a {@link List} of {@link ProductVariantDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductVariantDTO> findByCriteria(ProductVariantCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<ProductVariant> specification = createSpecification(criteria);
        return productVariantMapper.toDto(productVariantRepository.fetchBagRelationships(productVariantRepository.findAll(specification)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductVariantCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductVariant> specification = createSpecification(criteria);
        return productVariantRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductVariantCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductVariant> createSpecification(ProductVariantCriteria criteria) {
        Specification<ProductVariant> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductVariant_.id));
            }
            if (criteria.getCreatAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatAt(), ProductVariant_.creatAt));
            }
            if (criteria.getUpdateAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdateAt(), ProductVariant_.updateAt));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), ProductVariant_.price));
            }
            if (criteria.getAttributeValueId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAttributeValueId(), root ->
                        root.join(ProductVariant_.attributeValues, JoinType.LEFT).get(AttributeValue_.id)
                    )
                );
            }
        }
        return specification;
    }
}
