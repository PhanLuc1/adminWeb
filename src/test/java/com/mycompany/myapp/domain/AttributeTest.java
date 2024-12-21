package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AttributeTestSamples.*;
import static com.mycompany.myapp.domain.AttributeValueTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AttributeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attribute.class);
        Attribute attribute1 = getAttributeSample1();
        Attribute attribute2 = new Attribute();
        assertThat(attribute1).isNotEqualTo(attribute2);

        attribute2.setId(attribute1.getId());
        assertThat(attribute1).isEqualTo(attribute2);

        attribute2 = getAttributeSample2();
        assertThat(attribute1).isNotEqualTo(attribute2);
    }

    @Test
    void attributeValueTest() {
        Attribute attribute = getAttributeRandomSampleGenerator();
        AttributeValue attributeValueBack = getAttributeValueRandomSampleGenerator();

        attribute.addAttributeValue(attributeValueBack);
        assertThat(attribute.getAttributeValues()).containsOnly(attributeValueBack);
        assertThat(attributeValueBack.getAttribute()).isEqualTo(attribute);

        attribute.removeAttributeValue(attributeValueBack);
        assertThat(attribute.getAttributeValues()).doesNotContain(attributeValueBack);
        assertThat(attributeValueBack.getAttribute()).isNull();

        attribute.attributeValues(new HashSet<>(Set.of(attributeValueBack)));
        assertThat(attribute.getAttributeValues()).containsOnly(attributeValueBack);
        assertThat(attributeValueBack.getAttribute()).isEqualTo(attribute);

        attribute.setAttributeValues(new HashSet<>());
        assertThat(attribute.getAttributeValues()).doesNotContain(attributeValueBack);
        assertThat(attributeValueBack.getAttribute()).isNull();
    }
}
