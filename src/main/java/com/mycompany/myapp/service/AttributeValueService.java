package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.AttributeValueDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.AttributeValue}.
 */
public interface AttributeValueService {
    /**
     * Save a attributeValue.
     *
     * @param attributeValueDTO the entity to save.
     * @return the persisted entity.
     */
    AttributeValueDTO save(AttributeValueDTO attributeValueDTO);

    /**
     * Updates a attributeValue.
     *
     * @param attributeValueDTO the entity to update.
     * @return the persisted entity.
     */
    AttributeValueDTO update(AttributeValueDTO attributeValueDTO);

    /**
     * Partially updates a attributeValue.
     *
     * @param attributeValueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AttributeValueDTO> partialUpdate(AttributeValueDTO attributeValueDTO);

    /**
     * Get the "id" attributeValue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AttributeValueDTO> findOne(Long id);

    /**
     * Delete the "id" attributeValue.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
