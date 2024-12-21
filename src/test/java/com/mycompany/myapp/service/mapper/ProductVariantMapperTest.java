package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ProductVariantAsserts.*;
import static com.mycompany.myapp.domain.ProductVariantTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductVariantMapperTest {

    private ProductVariantMapper productVariantMapper;

    @BeforeEach
    void setUp() {
        productVariantMapper = new ProductVariantMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductVariantSample1();
        var actual = productVariantMapper.toEntity(productVariantMapper.toDto(expected));
        assertProductVariantAllPropertiesEquals(expected, actual);
    }
}
