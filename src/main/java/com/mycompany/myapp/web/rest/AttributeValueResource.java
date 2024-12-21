package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.AttributeValueRepository;
import com.mycompany.myapp.service.AttributeValueQueryService;
import com.mycompany.myapp.service.AttributeValueService;
import com.mycompany.myapp.service.criteria.AttributeValueCriteria;
import com.mycompany.myapp.service.dto.AttributeValueDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.AttributeValue}.
 */
@RestController
@RequestMapping("/api/attribute-values")
public class AttributeValueResource {

    private static final Logger LOG = LoggerFactory.getLogger(AttributeValueResource.class);

    private static final String ENTITY_NAME = "attributeValue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttributeValueService attributeValueService;

    private final AttributeValueRepository attributeValueRepository;

    private final AttributeValueQueryService attributeValueQueryService;

    public AttributeValueResource(
        AttributeValueService attributeValueService,
        AttributeValueRepository attributeValueRepository,
        AttributeValueQueryService attributeValueQueryService
    ) {
        this.attributeValueService = attributeValueService;
        this.attributeValueRepository = attributeValueRepository;
        this.attributeValueQueryService = attributeValueQueryService;
    }

    /**
     * {@code POST  /attribute-values} : Create a new attributeValue.
     *
     * @param attributeValueDTO the attributeValueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attributeValueDTO, or with status {@code 400 (Bad Request)} if the attributeValue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AttributeValueDTO> createAttributeValue(@RequestBody AttributeValueDTO attributeValueDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AttributeValue : {}", attributeValueDTO);
        if (attributeValueDTO.getId() != null) {
            throw new BadRequestAlertException("A new attributeValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        attributeValueDTO = attributeValueService.save(attributeValueDTO);
        return ResponseEntity.created(new URI("/api/attribute-values/" + attributeValueDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, attributeValueDTO.getId().toString()))
            .body(attributeValueDTO);
    }

    /**
     * {@code PUT  /attribute-values/:id} : Updates an existing attributeValue.
     *
     * @param id the id of the attributeValueDTO to save.
     * @param attributeValueDTO the attributeValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attributeValueDTO,
     * or with status {@code 400 (Bad Request)} if the attributeValueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attributeValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AttributeValueDTO> updateAttributeValue(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AttributeValueDTO attributeValueDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AttributeValue : {}, {}", id, attributeValueDTO);
        if (attributeValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attributeValueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attributeValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        attributeValueDTO = attributeValueService.update(attributeValueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attributeValueDTO.getId().toString()))
            .body(attributeValueDTO);
    }

    /**
     * {@code PATCH  /attribute-values/:id} : Partial updates given fields of an existing attributeValue, field will ignore if it is null
     *
     * @param id the id of the attributeValueDTO to save.
     * @param attributeValueDTO the attributeValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attributeValueDTO,
     * or with status {@code 400 (Bad Request)} if the attributeValueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the attributeValueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the attributeValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AttributeValueDTO> partialUpdateAttributeValue(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AttributeValueDTO attributeValueDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AttributeValue partially : {}, {}", id, attributeValueDTO);
        if (attributeValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attributeValueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attributeValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AttributeValueDTO> result = attributeValueService.partialUpdate(attributeValueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attributeValueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /attribute-values} : get all the attributeValues.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attributeValues in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AttributeValueDTO>> getAllAttributeValues(
        AttributeValueCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AttributeValues by criteria: {}", criteria);

        Page<AttributeValueDTO> page = attributeValueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /attribute-values/count} : count all the attributeValues.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAttributeValues(AttributeValueCriteria criteria) {
        LOG.debug("REST request to count AttributeValues by criteria: {}", criteria);
        return ResponseEntity.ok().body(attributeValueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /attribute-values/:id} : get the "id" attributeValue.
     *
     * @param id the id of the attributeValueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attributeValueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AttributeValueDTO> getAttributeValue(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AttributeValue : {}", id);
        Optional<AttributeValueDTO> attributeValueDTO = attributeValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attributeValueDTO);
    }

    /**
     * {@code DELETE  /attribute-values/:id} : delete the "id" attributeValue.
     *
     * @param id the id of the attributeValueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttributeValue(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AttributeValue : {}", id);
        attributeValueService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
