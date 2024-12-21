package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttributeValueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttributeValueDTO.class);
        AttributeValueDTO attributeValueDTO1 = new AttributeValueDTO();
        attributeValueDTO1.setId(1L);
        AttributeValueDTO attributeValueDTO2 = new AttributeValueDTO();
        assertThat(attributeValueDTO1).isNotEqualTo(attributeValueDTO2);
        attributeValueDTO2.setId(attributeValueDTO1.getId());
        assertThat(attributeValueDTO1).isEqualTo(attributeValueDTO2);
        attributeValueDTO2.setId(2L);
        assertThat(attributeValueDTO1).isNotEqualTo(attributeValueDTO2);
        attributeValueDTO1.setId(null);
        assertThat(attributeValueDTO1).isNotEqualTo(attributeValueDTO2);
    }
}
