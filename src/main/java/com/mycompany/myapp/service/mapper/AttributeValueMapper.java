package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Attribute;
import com.mycompany.myapp.domain.AttributeValue;
import com.mycompany.myapp.service.dto.AttributeDTO;
import com.mycompany.myapp.service.dto.AttributeValueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AttributeValue} and its DTO {@link AttributeValueDTO}.
 */
@Mapper(componentModel = "spring")
public interface AttributeValueMapper extends EntityMapper<AttributeValueDTO, AttributeValue> {
    @Mapping(target = "attribute", source = "attribute", qualifiedByName = "attributeId")
    AttributeValueDTO toDto(AttributeValue s);

    @Named("attributeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AttributeDTO toDtoAttributeId(Attribute attribute);
}
