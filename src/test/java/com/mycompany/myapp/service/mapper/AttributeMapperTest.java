package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.AttributeAsserts.*;
import static com.mycompany.myapp.domain.AttributeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AttributeMapperTest {

    private AttributeMapper attributeMapper;

    @BeforeEach
    void setUp() {
        attributeMapper = new AttributeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAttributeSample1();
        var actual = attributeMapper.toEntity(attributeMapper.toDto(expected));
        assertAttributeAllPropertiesEquals(expected, actual);
    }
}
