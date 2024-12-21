package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.AttributeValueAsserts.*;
import static com.mycompany.myapp.domain.AttributeValueTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AttributeValueMapperTest {

    private AttributeValueMapper attributeValueMapper;

    @BeforeEach
    void setUp() {
        attributeValueMapper = new AttributeValueMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAttributeValueSample1();
        var actual = attributeValueMapper.toEntity(attributeValueMapper.toDto(expected));
        assertAttributeValueAllPropertiesEquals(expected, actual);
    }
}
