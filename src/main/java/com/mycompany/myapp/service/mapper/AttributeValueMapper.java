package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Attribute;
import com.mycompany.myapp.domain.AttributeValue;
import com.mycompany.myapp.domain.ProductVariant;
import com.mycompany.myapp.service.dto.AttributeDTO;
import com.mycompany.myapp.service.dto.AttributeValueDTO;
import com.mycompany.myapp.service.dto.ProductVariantDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AttributeValue} and its DTO {@link AttributeValueDTO}.
 */
@Mapper(componentModel = "spring")
public interface AttributeValueMapper extends EntityMapper<AttributeValueDTO, AttributeValue> {
    @Mapping(target = "attribute", source = "attribute", qualifiedByName = "attributeId")
    @Mapping(target = "productVariants", source = "productVariants", qualifiedByName = "productVariantIdSet")
    AttributeValueDTO toDto(AttributeValue s);

    @Mapping(target = "productVariants", ignore = true)
    @Mapping(target = "removeProductVariant", ignore = true)
    AttributeValue toEntity(AttributeValueDTO attributeValueDTO);

    @Named("attributeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AttributeDTO toDtoAttributeId(Attribute attribute);

    @Named("productVariantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductVariantDTO toDtoProductVariantId(ProductVariant productVariant);

    @Named("productVariantIdSet")
    default Set<ProductVariantDTO> toDtoProductVariantIdSet(Set<ProductVariant> productVariant) {
        return productVariant.stream().map(this::toDtoProductVariantId).collect(Collectors.toSet());
    }
}
