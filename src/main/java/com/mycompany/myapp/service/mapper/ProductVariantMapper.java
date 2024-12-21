package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.AttributeValue;
import com.mycompany.myapp.domain.ProductVariant;
import com.mycompany.myapp.service.dto.AttributeValueDTO;
import com.mycompany.myapp.service.dto.ProductVariantDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductVariant} and its DTO {@link ProductVariantDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductVariantMapper extends EntityMapper<ProductVariantDTO, ProductVariant> {
    @Mapping(target = "attributeValues", source = "attributeValues", qualifiedByName = "attributeValueIdSet")
    ProductVariantDTO toDto(ProductVariant s);

    @Mapping(target = "removeAttributeValue", ignore = true)
    ProductVariant toEntity(ProductVariantDTO productVariantDTO);

    @Named("attributeValueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AttributeValueDTO toDtoAttributeValueId(AttributeValue attributeValue);

    @Named("attributeValueIdSet")
    default Set<AttributeValueDTO> toDtoAttributeValueIdSet(Set<AttributeValue> attributeValue) {
        return attributeValue.stream().map(this::toDtoAttributeValueId).collect(Collectors.toSet());
    }
}
