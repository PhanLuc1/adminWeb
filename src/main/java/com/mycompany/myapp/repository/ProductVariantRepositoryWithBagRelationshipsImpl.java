package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductVariant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ProductVariantRepositoryWithBagRelationshipsImpl implements ProductVariantRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String PRODUCTVARIANTS_PARAMETER = "productVariants";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<ProductVariant> fetchBagRelationships(Optional<ProductVariant> productVariant) {
        return productVariant.map(this::fetchAttributeValues);
    }

    @Override
    public Page<ProductVariant> fetchBagRelationships(Page<ProductVariant> productVariants) {
        return new PageImpl<>(
            fetchBagRelationships(productVariants.getContent()),
            productVariants.getPageable(),
            productVariants.getTotalElements()
        );
    }

    @Override
    public List<ProductVariant> fetchBagRelationships(List<ProductVariant> productVariants) {
        return Optional.of(productVariants).map(this::fetchAttributeValues).orElse(Collections.emptyList());
    }

    ProductVariant fetchAttributeValues(ProductVariant result) {
        return entityManager
            .createQuery(
                "select productVariant from ProductVariant productVariant left join fetch productVariant.attributeValues where productVariant.id = :id",
                ProductVariant.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<ProductVariant> fetchAttributeValues(List<ProductVariant> productVariants) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, productVariants.size()).forEach(index -> order.put(productVariants.get(index).getId(), index));
        List<ProductVariant> result = entityManager
            .createQuery(
                "select productVariant from ProductVariant productVariant left join fetch productVariant.attributeValues where productVariant in :productVariants",
                ProductVariant.class
            )
            .setParameter(PRODUCTVARIANTS_PARAMETER, productVariants)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
