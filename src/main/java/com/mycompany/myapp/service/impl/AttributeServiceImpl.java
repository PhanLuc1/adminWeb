package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Attribute;
import com.mycompany.myapp.repository.AttributeRepository;
import com.mycompany.myapp.service.AttributeService;
import com.mycompany.myapp.service.dto.AttributeDTO;
import com.mycompany.myapp.service.mapper.AttributeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Attribute}.
 */
@Service
@Transactional
public class AttributeServiceImpl implements AttributeService {

    private static final Logger LOG = LoggerFactory.getLogger(AttributeServiceImpl.class);

    private final AttributeRepository attributeRepository;

    private final AttributeMapper attributeMapper;

    public AttributeServiceImpl(AttributeRepository attributeRepository, AttributeMapper attributeMapper) {
        this.attributeRepository = attributeRepository;
        this.attributeMapper = attributeMapper;
    }

    @Override
    public AttributeDTO save(AttributeDTO attributeDTO) {
        LOG.debug("Request to save Attribute : {}", attributeDTO);
        Attribute attribute = attributeMapper.toEntity(attributeDTO);
        attribute = attributeRepository.save(attribute);
        return attributeMapper.toDto(attribute);
    }

    @Override
    public AttributeDTO update(AttributeDTO attributeDTO) {
        LOG.debug("Request to update Attribute : {}", attributeDTO);
        Attribute attribute = attributeMapper.toEntity(attributeDTO);
        attribute = attributeRepository.save(attribute);
        return attributeMapper.toDto(attribute);
    }

    @Override
    public Optional<AttributeDTO> partialUpdate(AttributeDTO attributeDTO) {
        LOG.debug("Request to partially update Attribute : {}", attributeDTO);

        return attributeRepository
            .findById(attributeDTO.getId())
            .map(existingAttribute -> {
                attributeMapper.partialUpdate(existingAttribute, attributeDTO);

                return existingAttribute;
            })
            .map(attributeRepository::save)
            .map(attributeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AttributeDTO> findOne(Long id) {
        LOG.debug("Request to get Attribute : {}", id);
        return attributeRepository.findById(id).map(attributeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Attribute : {}", id);
        attributeRepository.deleteById(id);
    }
}
