package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.AttributeAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Attribute;
import com.mycompany.myapp.repository.AttributeRepository;
import com.mycompany.myapp.service.dto.AttributeDTO;
import com.mycompany.myapp.service.mapper.AttributeMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AttributeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AttributeResourceIT {

    private static final Instant DEFAULT_CREATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/attributes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private AttributeMapper attributeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttributeMockMvc;

    private Attribute attribute;

    private Attribute insertedAttribute;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attribute createEntity() {
        return new Attribute().createAt(DEFAULT_CREATE_AT).updateAt(DEFAULT_UPDATE_AT).name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attribute createUpdatedEntity() {
        return new Attribute().createAt(UPDATED_CREATE_AT).updateAt(UPDATED_UPDATE_AT).name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        attribute = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAttribute != null) {
            attributeRepository.delete(insertedAttribute);
            insertedAttribute = null;
        }
    }

    @Test
    @Transactional
    void createAttribute() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Attribute
        AttributeDTO attributeDTO = attributeMapper.toDto(attribute);
        var returnedAttributeDTO = om.readValue(
            restAttributeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attributeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AttributeDTO.class
        );

        // Validate the Attribute in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAttribute = attributeMapper.toEntity(returnedAttributeDTO);
        assertAttributeUpdatableFieldsEquals(returnedAttribute, getPersistedAttribute(returnedAttribute));

        insertedAttribute = returnedAttribute;
    }

    @Test
    @Transactional
    void createAttributeWithExistingId() throws Exception {
        // Create the Attribute with an existing ID
        attribute.setId(1L);
        AttributeDTO attributeDTO = attributeMapper.toDto(attribute);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttributeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attributeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Attribute in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAttributes() throws Exception {
        // Initialize the database
        insertedAttribute = attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList
        restAttributeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attribute.getId().intValue())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getAttribute() throws Exception {
        // Initialize the database
        insertedAttribute = attributeRepository.saveAndFlush(attribute);

        // Get the attribute
        restAttributeMockMvc
            .perform(get(ENTITY_API_URL_ID, attribute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attribute.getId().intValue()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getAttributesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAttribute = attributeRepository.saveAndFlush(attribute);

        Long id = attribute.getId();

        defaultAttributeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAttributeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAttributeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAttributesByCreateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAttribute = attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where createAt equals to
        defaultAttributeFiltering("createAt.equals=" + DEFAULT_CREATE_AT, "createAt.equals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllAttributesByCreateAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAttribute = attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where createAt in
        defaultAttributeFiltering("createAt.in=" + DEFAULT_CREATE_AT + "," + UPDATED_CREATE_AT, "createAt.in=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllAttributesByCreateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAttribute = attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where createAt is not null
        defaultAttributeFiltering("createAt.specified=true", "createAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAttributesByUpdateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAttribute = attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where updateAt equals to
        defaultAttributeFiltering("updateAt.equals=" + DEFAULT_UPDATE_AT, "updateAt.equals=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllAttributesByUpdateAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAttribute = attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where updateAt in
        defaultAttributeFiltering("updateAt.in=" + DEFAULT_UPDATE_AT + "," + UPDATED_UPDATE_AT, "updateAt.in=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllAttributesByUpdateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAttribute = attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where updateAt is not null
        defaultAttributeFiltering("updateAt.specified=true", "updateAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAttributesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAttribute = attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where name equals to
        defaultAttributeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAttributesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAttribute = attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where name in
        defaultAttributeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAttributesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAttribute = attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where name is not null
        defaultAttributeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAttributesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAttribute = attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where name contains
        defaultAttributeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAttributesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAttribute = attributeRepository.saveAndFlush(attribute);

        // Get all the attributeList where name does not contain
        defaultAttributeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    private void defaultAttributeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAttributeShouldBeFound(shouldBeFound);
        defaultAttributeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAttributeShouldBeFound(String filter) throws Exception {
        restAttributeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attribute.getId().intValue())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restAttributeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAttributeShouldNotBeFound(String filter) throws Exception {
        restAttributeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAttributeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAttribute() throws Exception {
        // Get the attribute
        restAttributeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAttribute() throws Exception {
        // Initialize the database
        insertedAttribute = attributeRepository.saveAndFlush(attribute);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attribute
        Attribute updatedAttribute = attributeRepository.findById(attribute.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAttribute are not directly saved in db
        em.detach(updatedAttribute);
        updatedAttribute.createAt(UPDATED_CREATE_AT).updateAt(UPDATED_UPDATE_AT).name(UPDATED_NAME);
        AttributeDTO attributeDTO = attributeMapper.toDto(updatedAttribute);

        restAttributeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attributeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(attributeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Attribute in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAttributeToMatchAllProperties(updatedAttribute);
    }

    @Test
    @Transactional
    void putNonExistingAttribute() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attribute.setId(longCount.incrementAndGet());

        // Create the Attribute
        AttributeDTO attributeDTO = attributeMapper.toDto(attribute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attributeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(attributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attribute in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAttribute() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attribute.setId(longCount.incrementAndGet());

        // Create the Attribute
        AttributeDTO attributeDTO = attributeMapper.toDto(attribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(attributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attribute in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAttribute() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attribute.setId(longCount.incrementAndGet());

        // Create the Attribute
        AttributeDTO attributeDTO = attributeMapper.toDto(attribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attributeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Attribute in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAttributeWithPatch() throws Exception {
        // Initialize the database
        insertedAttribute = attributeRepository.saveAndFlush(attribute);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attribute using partial update
        Attribute partialUpdatedAttribute = new Attribute();
        partialUpdatedAttribute.setId(attribute.getId());

        partialUpdatedAttribute.updateAt(UPDATED_UPDATE_AT);

        restAttributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttribute.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAttribute))
            )
            .andExpect(status().isOk());

        // Validate the Attribute in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAttributeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAttribute, attribute),
            getPersistedAttribute(attribute)
        );
    }

    @Test
    @Transactional
    void fullUpdateAttributeWithPatch() throws Exception {
        // Initialize the database
        insertedAttribute = attributeRepository.saveAndFlush(attribute);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attribute using partial update
        Attribute partialUpdatedAttribute = new Attribute();
        partialUpdatedAttribute.setId(attribute.getId());

        partialUpdatedAttribute.createAt(UPDATED_CREATE_AT).updateAt(UPDATED_UPDATE_AT).name(UPDATED_NAME);

        restAttributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttribute.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAttribute))
            )
            .andExpect(status().isOk());

        // Validate the Attribute in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAttributeUpdatableFieldsEquals(partialUpdatedAttribute, getPersistedAttribute(partialUpdatedAttribute));
    }

    @Test
    @Transactional
    void patchNonExistingAttribute() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attribute.setId(longCount.incrementAndGet());

        // Create the Attribute
        AttributeDTO attributeDTO = attributeMapper.toDto(attribute);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attributeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(attributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attribute in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAttribute() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attribute.setId(longCount.incrementAndGet());

        // Create the Attribute
        AttributeDTO attributeDTO = attributeMapper.toDto(attribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(attributeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attribute in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAttribute() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attribute.setId(longCount.incrementAndGet());

        // Create the Attribute
        AttributeDTO attributeDTO = attributeMapper.toDto(attribute);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(attributeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Attribute in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAttribute() throws Exception {
        // Initialize the database
        insertedAttribute = attributeRepository.saveAndFlush(attribute);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the attribute
        restAttributeMockMvc
            .perform(delete(ENTITY_API_URL_ID, attribute.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return attributeRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Attribute getPersistedAttribute(Attribute attribute) {
        return attributeRepository.findById(attribute.getId()).orElseThrow();
    }

    protected void assertPersistedAttributeToMatchAllProperties(Attribute expectedAttribute) {
        assertAttributeAllPropertiesEquals(expectedAttribute, getPersistedAttribute(expectedAttribute));
    }

    protected void assertPersistedAttributeToMatchUpdatableProperties(Attribute expectedAttribute) {
        assertAttributeAllUpdatablePropertiesEquals(expectedAttribute, getPersistedAttribute(expectedAttribute));
    }
}
