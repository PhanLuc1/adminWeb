package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AttributeValueTestSamples.*;
import static com.mycompany.myapp.domain.ProductVariantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductVariantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductVariant.class);
        ProductVariant productVariant1 = getProductVariantSample1();
        ProductVariant productVariant2 = new ProductVariant();
        assertThat(productVariant1).isNotEqualTo(productVariant2);

        productVariant2.setId(productVariant1.getId());
        assertThat(productVariant1).isEqualTo(productVariant2);

        productVariant2 = getProductVariantSample2();
        assertThat(productVariant1).isNotEqualTo(productVariant2);
    }

    @Test
    void attributeValueTest() {
        ProductVariant productVariant = getProductVariantRandomSampleGenerator();
        AttributeValue attributeValueBack = getAttributeValueRandomSampleGenerator();

        productVariant.addAttributeValue(attributeValueBack);
        assertThat(productVariant.getAttributeValues()).containsOnly(attributeValueBack);

        productVariant.removeAttributeValue(attributeValueBack);
        assertThat(productVariant.getAttributeValues()).doesNotContain(attributeValueBack);

        productVariant.attributeValues(new HashSet<>(Set.of(attributeValueBack)));
        assertThat(productVariant.getAttributeValues()).containsOnly(attributeValueBack);

        productVariant.setAttributeValues(new HashSet<>());
        assertThat(productVariant.getAttributeValues()).doesNotContain(attributeValueBack);
    }
}
