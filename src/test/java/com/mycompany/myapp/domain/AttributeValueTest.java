package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AttributeTestSamples.*;
import static com.mycompany.myapp.domain.AttributeValueTestSamples.*;
import static com.mycompany.myapp.domain.ProductVariantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AttributeValueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttributeValue.class);
        AttributeValue attributeValue1 = getAttributeValueSample1();
        AttributeValue attributeValue2 = new AttributeValue();
        assertThat(attributeValue1).isNotEqualTo(attributeValue2);

        attributeValue2.setId(attributeValue1.getId());
        assertThat(attributeValue1).isEqualTo(attributeValue2);

        attributeValue2 = getAttributeValueSample2();
        assertThat(attributeValue1).isNotEqualTo(attributeValue2);
    }

    @Test
    void attributeTest() {
        AttributeValue attributeValue = getAttributeValueRandomSampleGenerator();
        Attribute attributeBack = getAttributeRandomSampleGenerator();

        attributeValue.setAttribute(attributeBack);
        assertThat(attributeValue.getAttribute()).isEqualTo(attributeBack);

        attributeValue.attribute(null);
        assertThat(attributeValue.getAttribute()).isNull();
    }

    @Test
    void productVariantTest() {
        AttributeValue attributeValue = getAttributeValueRandomSampleGenerator();
        ProductVariant productVariantBack = getProductVariantRandomSampleGenerator();

        attributeValue.addProductVariant(productVariantBack);
        assertThat(attributeValue.getProductVariants()).containsOnly(productVariantBack);
        assertThat(productVariantBack.getAttributeValues()).containsOnly(attributeValue);

        attributeValue.removeProductVariant(productVariantBack);
        assertThat(attributeValue.getProductVariants()).doesNotContain(productVariantBack);
        assertThat(productVariantBack.getAttributeValues()).doesNotContain(attributeValue);

        attributeValue.productVariants(new HashSet<>(Set.of(productVariantBack)));
        assertThat(attributeValue.getProductVariants()).containsOnly(productVariantBack);
        assertThat(productVariantBack.getAttributeValues()).containsOnly(attributeValue);

        attributeValue.setProductVariants(new HashSet<>());
        assertThat(attributeValue.getProductVariants()).doesNotContain(productVariantBack);
        assertThat(productVariantBack.getAttributeValues()).doesNotContain(attributeValue);
    }
}
