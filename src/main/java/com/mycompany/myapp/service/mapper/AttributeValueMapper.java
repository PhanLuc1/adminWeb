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
    @Mapping(target = "attribute", source = "attribute") // Ánh xạ toàn bộ đối tượng Attribute
    AttributeValueDTO toDto(AttributeValue s);

    // Ánh xạ đầy đủ các thuộc tính của Attribute
    @Named("attributeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "createAt", source = "createAt")
    @Mapping(target = "updateAt", source = "updateAt")
    AttributeDTO toDtoAttributeId(Attribute attribute);
}
