package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductVariant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ProductVariantRepositoryWithBagRelationships {
    Optional<ProductVariant> fetchBagRelationships(Optional<ProductVariant> productVariant);

    List<ProductVariant> fetchBagRelationships(List<ProductVariant> productVariants);

    Page<ProductVariant> fetchBagRelationships(Page<ProductVariant> productVariants);
}
