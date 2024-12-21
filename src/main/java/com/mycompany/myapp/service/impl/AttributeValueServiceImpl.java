package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.AttributeValue;
import com.mycompany.myapp.repository.AttributeValueRepository;
import com.mycompany.myapp.service.AttributeValueService;
import com.mycompany.myapp.service.dto.AttributeValueDTO;
import com.mycompany.myapp.service.mapper.AttributeValueMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.AttributeValue}.
 */
@Service
@Transactional
public class AttributeValueServiceImpl implements AttributeValueService {

    private static final Logger LOG = LoggerFactory.getLogger(AttributeValueServiceImpl.class);

    private final AttributeValueRepository attributeValueRepository;

    private final AttributeValueMapper attributeValueMapper;

    public AttributeValueServiceImpl(AttributeValueRepository attributeValueRepository, AttributeValueMapper attributeValueMapper) {
        this.attributeValueRepository = attributeValueRepository;
        this.attributeValueMapper = attributeValueMapper;
    }

    @Override
    public AttributeValueDTO save(AttributeValueDTO attributeValueDTO) {
        LOG.debug("Request to save AttributeValue : {}", attributeValueDTO);
        AttributeValue attributeValue = attributeValueMapper.toEntity(attributeValueDTO);
        attributeValue = attributeValueRepository.save(attributeValue);
        return attributeValueMapper.toDto(attributeValue);
    }

    @Override
    public AttributeValueDTO update(AttributeValueDTO attributeValueDTO) {
        LOG.debug("Request to update AttributeValue : {}", attributeValueDTO);
        AttributeValue attributeValue = attributeValueMapper.toEntity(attributeValueDTO);
        attributeValue = attributeValueRepository.save(attributeValue);
        return attributeValueMapper.toDto(attributeValue);
    }

    @Override
    public Optional<AttributeValueDTO> partialUpdate(AttributeValueDTO attributeValueDTO) {
        LOG.debug("Request to partially update AttributeValue : {}", attributeValueDTO);

        return attributeValueRepository
            .findById(attributeValueDTO.getId())
            .map(existingAttributeValue -> {
                attributeValueMapper.partialUpdate(existingAttributeValue, attributeValueDTO);

                return existingAttributeValue;
            })
            .map(attributeValueRepository::save)
            .map(attributeValueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AttributeValueDTO> findOne(Long id) {
        LOG.debug("Request to get AttributeValue : {}", id);
        return attributeValueRepository.findById(id).map(attributeValueMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AttributeValue : {}", id);
        attributeValueRepository.deleteById(id);
    }
}
