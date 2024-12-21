package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductVariant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductVariant entity.
 *
 * When extending this class, extend ProductVariantRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ProductVariantRepository
    extends ProductVariantRepositoryWithBagRelationships, JpaRepository<ProductVariant, Long>, JpaSpecificationExecutor<ProductVariant> {
    default Optional<ProductVariant> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<ProductVariant> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<ProductVariant> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
