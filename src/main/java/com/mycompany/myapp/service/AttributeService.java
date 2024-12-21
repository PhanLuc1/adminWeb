package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.AttributeDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Attribute}.
 */
public interface AttributeService {
    /**
     * Save a attribute.
     *
     * @param attributeDTO the entity to save.
     * @return the persisted entity.
     */
    AttributeDTO save(AttributeDTO attributeDTO);

    /**
     * Updates a attribute.
     *
     * @param attributeDTO the entity to update.
     * @return the persisted entity.
     */
    AttributeDTO update(AttributeDTO attributeDTO);

    /**
     * Partially updates a attribute.
     *
     * @param attributeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AttributeDTO> partialUpdate(AttributeDTO attributeDTO);

    /**
     * Get the "id" attribute.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AttributeDTO> findOne(Long id);

    /**
     * Delete the "id" attribute.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
