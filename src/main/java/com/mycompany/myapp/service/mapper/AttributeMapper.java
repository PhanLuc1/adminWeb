package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Attribute;
import com.mycompany.myapp.service.dto.AttributeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Attribute} and its DTO {@link AttributeDTO}.
 */
@Mapper(componentModel = "spring")
public interface AttributeMapper extends EntityMapper<AttributeDTO, Attribute> {}
