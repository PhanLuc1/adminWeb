package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.AttributeRepository;
import com.mycompany.myapp.service.AttributeQueryService;
import com.mycompany.myapp.service.AttributeService;
import com.mycompany.myapp.service.criteria.AttributeCriteria;
import com.mycompany.myapp.service.dto.AttributeDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Attribute}.
 */
@RestController
@RequestMapping("/api/attributes")
public class AttributeResource {

    private static final Logger LOG = LoggerFactory.getLogger(AttributeResource.class);

    private static final String ENTITY_NAME = "attribute";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttributeService attributeService;

    private final AttributeRepository attributeRepository;

    private final AttributeQueryService attributeQueryService;

    public AttributeResource(
        AttributeService attributeService,
        AttributeRepository attributeRepository,
        AttributeQueryService attributeQueryService
    ) {
        this.attributeService = attributeService;
        this.attributeRepository = attributeRepository;
        this.attributeQueryService = attributeQueryService;
    }

    /**
     * {@code POST  /attributes} : Create a new attribute.
     *
     * @param attributeDTO the attributeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attributeDTO, or with status {@code 400 (Bad Request)} if the attribute has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AttributeDTO> createAttribute(@RequestBody AttributeDTO attributeDTO) throws URISyntaxException {
        LOG.debug("REST request to save Attribute : {}", attributeDTO);
        if (attributeDTO.getId() != null) {
            throw new BadRequestAlertException("A new attribute cannot already have an ID", ENTITY_NAME, "idexists");
        }
        attributeDTO = attributeService.save(attributeDTO);
        return ResponseEntity.created(new URI("/api/attributes/" + attributeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, attributeDTO.getId().toString()))
            .body(attributeDTO);
    }

    /**
     * {@code PUT  /attributes/:id} : Updates an existing attribute.
     *
     * @param id the id of the attributeDTO to save.
     * @param attributeDTO the attributeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attributeDTO,
     * or with status {@code 400 (Bad Request)} if the attributeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attributeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AttributeDTO> updateAttribute(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AttributeDTO attributeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Attribute : {}, {}", id, attributeDTO);
        if (attributeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attributeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attributeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        attributeDTO = attributeService.update(attributeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attributeDTO.getId().toString()))
            .body(attributeDTO);
    }

    /**
     * {@code PATCH  /attributes/:id} : Partial updates given fields of an existing attribute, field will ignore if it is null
     *
     * @param id the id of the attributeDTO to save.
     * @param attributeDTO the attributeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attributeDTO,
     * or with status {@code 400 (Bad Request)} if the attributeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the attributeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the attributeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AttributeDTO> partialUpdateAttribute(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AttributeDTO attributeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Attribute partially : {}, {}", id, attributeDTO);
        if (attributeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attributeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attributeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AttributeDTO> result = attributeService.partialUpdate(attributeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attributeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /attributes} : get all the attributes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attributes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AttributeDTO>> getAllAttributes(
        AttributeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Attributes by criteria: {}", criteria);

        Page<AttributeDTO> page = attributeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /attributes/count} : count all the attributes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAttributes(AttributeCriteria criteria) {
        LOG.debug("REST request to count Attributes by criteria: {}", criteria);
        return ResponseEntity.ok().body(attributeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /attributes/:id} : get the "id" attribute.
     *
     * @param id the id of the attributeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attributeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AttributeDTO> getAttribute(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Attribute : {}", id);
        Optional<AttributeDTO> attributeDTO = attributeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attributeDTO);
    }

    /**
     * {@code DELETE  /attributes/:id} : delete the "id" attribute.
     *
     * @param id the id of the attributeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttribute(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Attribute : {}", id);
        attributeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
