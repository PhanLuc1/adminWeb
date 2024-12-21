package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.AttributeValueAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Attribute;
import com.mycompany.myapp.domain.AttributeValue;
import com.mycompany.myapp.repository.AttributeValueRepository;
import com.mycompany.myapp.service.dto.AttributeValueDTO;
import com.mycompany.myapp.service.mapper.AttributeValueMapper;
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
 * Integration tests for the {@link AttributeValueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AttributeValueResourceIT {

    private static final Instant DEFAULT_CREAT_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREAT_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/attribute-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AttributeValueRepository attributeValueRepository;

    @Autowired
    private AttributeValueMapper attributeValueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttributeValueMockMvc;

    private AttributeValue attributeValue;

    private AttributeValue insertedAttributeValue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttributeValue createEntity() {
        return new AttributeValue().creatAt(DEFAULT_CREAT_AT).updateAt(DEFAULT_UPDATE_AT).name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttributeValue createUpdatedEntity() {
        return new AttributeValue().creatAt(UPDATED_CREAT_AT).updateAt(UPDATED_UPDATE_AT).name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        attributeValue = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAttributeValue != null) {
            attributeValueRepository.delete(insertedAttributeValue);
            insertedAttributeValue = null;
        }
    }

    @Test
    @Transactional
    void createAttributeValue() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AttributeValue
        AttributeValueDTO attributeValueDTO = attributeValueMapper.toDto(attributeValue);
        var returnedAttributeValueDTO = om.readValue(
            restAttributeValueMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attributeValueDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AttributeValueDTO.class
        );

        // Validate the AttributeValue in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAttributeValue = attributeValueMapper.toEntity(returnedAttributeValueDTO);
        assertAttributeValueUpdatableFieldsEquals(returnedAttributeValue, getPersistedAttributeValue(returnedAttributeValue));

        insertedAttributeValue = returnedAttributeValue;
    }

    @Test
    @Transactional
    void createAttributeValueWithExistingId() throws Exception {
        // Create the AttributeValue with an existing ID
        attributeValue.setId(1L);
        AttributeValueDTO attributeValueDTO = attributeValueMapper.toDto(attributeValue);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttributeValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attributeValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AttributeValue in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAttributeValues() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        // Get all the attributeValueList
        restAttributeValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attributeValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].creatAt").value(hasItem(DEFAULT_CREAT_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getAttributeValue() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        // Get the attributeValue
        restAttributeValueMockMvc
            .perform(get(ENTITY_API_URL_ID, attributeValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attributeValue.getId().intValue()))
            .andExpect(jsonPath("$.creatAt").value(DEFAULT_CREAT_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getAttributeValuesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        Long id = attributeValue.getId();

        defaultAttributeValueFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAttributeValueFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAttributeValueFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAttributeValuesByCreatAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        // Get all the attributeValueList where creatAt equals to
        defaultAttributeValueFiltering("creatAt.equals=" + DEFAULT_CREAT_AT, "creatAt.equals=" + UPDATED_CREAT_AT);
    }

    @Test
    @Transactional
    void getAllAttributeValuesByCreatAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        // Get all the attributeValueList where creatAt in
        defaultAttributeValueFiltering("creatAt.in=" + DEFAULT_CREAT_AT + "," + UPDATED_CREAT_AT, "creatAt.in=" + UPDATED_CREAT_AT);
    }

    @Test
    @Transactional
    void getAllAttributeValuesByCreatAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        // Get all the attributeValueList where creatAt is not null
        defaultAttributeValueFiltering("creatAt.specified=true", "creatAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAttributeValuesByUpdateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        // Get all the attributeValueList where updateAt equals to
        defaultAttributeValueFiltering("updateAt.equals=" + DEFAULT_UPDATE_AT, "updateAt.equals=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllAttributeValuesByUpdateAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        // Get all the attributeValueList where updateAt in
        defaultAttributeValueFiltering("updateAt.in=" + DEFAULT_UPDATE_AT + "," + UPDATED_UPDATE_AT, "updateAt.in=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllAttributeValuesByUpdateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        // Get all the attributeValueList where updateAt is not null
        defaultAttributeValueFiltering("updateAt.specified=true", "updateAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAttributeValuesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        // Get all the attributeValueList where name equals to
        defaultAttributeValueFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAttributeValuesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        // Get all the attributeValueList where name in
        defaultAttributeValueFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAttributeValuesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        // Get all the attributeValueList where name is not null
        defaultAttributeValueFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAttributeValuesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        // Get all the attributeValueList where name contains
        defaultAttributeValueFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAttributeValuesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        // Get all the attributeValueList where name does not contain
        defaultAttributeValueFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAttributeValuesByAttributeIsEqualToSomething() throws Exception {
        Attribute attribute;
        if (TestUtil.findAll(em, Attribute.class).isEmpty()) {
            attributeValueRepository.saveAndFlush(attributeValue);
            attribute = AttributeResourceIT.createEntity();
        } else {
            attribute = TestUtil.findAll(em, Attribute.class).get(0);
        }
        em.persist(attribute);
        em.flush();
        attributeValue.setAttribute(attribute);
        attributeValueRepository.saveAndFlush(attributeValue);
        Long attributeId = attribute.getId();
        // Get all the attributeValueList where attribute equals to attributeId
        defaultAttributeValueShouldBeFound("attributeId.equals=" + attributeId);

        // Get all the attributeValueList where attribute equals to (attributeId + 1)
        defaultAttributeValueShouldNotBeFound("attributeId.equals=" + (attributeId + 1));
    }

    private void defaultAttributeValueFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAttributeValueShouldBeFound(shouldBeFound);
        defaultAttributeValueShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAttributeValueShouldBeFound(String filter) throws Exception {
        restAttributeValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attributeValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].creatAt").value(hasItem(DEFAULT_CREAT_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restAttributeValueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAttributeValueShouldNotBeFound(String filter) throws Exception {
        restAttributeValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAttributeValueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAttributeValue() throws Exception {
        // Get the attributeValue
        restAttributeValueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAttributeValue() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attributeValue
        AttributeValue updatedAttributeValue = attributeValueRepository.findById(attributeValue.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAttributeValue are not directly saved in db
        em.detach(updatedAttributeValue);
        updatedAttributeValue.creatAt(UPDATED_CREAT_AT).updateAt(UPDATED_UPDATE_AT).name(UPDATED_NAME);
        AttributeValueDTO attributeValueDTO = attributeValueMapper.toDto(updatedAttributeValue);

        restAttributeValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attributeValueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(attributeValueDTO))
            )
            .andExpect(status().isOk());

        // Validate the AttributeValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAttributeValueToMatchAllProperties(updatedAttributeValue);
    }

    @Test
    @Transactional
    void putNonExistingAttributeValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attributeValue.setId(longCount.incrementAndGet());

        // Create the AttributeValue
        AttributeValueDTO attributeValueDTO = attributeValueMapper.toDto(attributeValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributeValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attributeValueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(attributeValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributeValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAttributeValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attributeValue.setId(longCount.incrementAndGet());

        // Create the AttributeValue
        AttributeValueDTO attributeValueDTO = attributeValueMapper.toDto(attributeValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(attributeValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributeValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAttributeValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attributeValue.setId(longCount.incrementAndGet());

        // Create the AttributeValue
        AttributeValueDTO attributeValueDTO = attributeValueMapper.toDto(attributeValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeValueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attributeValueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttributeValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAttributeValueWithPatch() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attributeValue using partial update
        AttributeValue partialUpdatedAttributeValue = new AttributeValue();
        partialUpdatedAttributeValue.setId(attributeValue.getId());

        partialUpdatedAttributeValue.creatAt(UPDATED_CREAT_AT).name(UPDATED_NAME);

        restAttributeValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttributeValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAttributeValue))
            )
            .andExpect(status().isOk());

        // Validate the AttributeValue in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAttributeValueUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAttributeValue, attributeValue),
            getPersistedAttributeValue(attributeValue)
        );
    }

    @Test
    @Transactional
    void fullUpdateAttributeValueWithPatch() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attributeValue using partial update
        AttributeValue partialUpdatedAttributeValue = new AttributeValue();
        partialUpdatedAttributeValue.setId(attributeValue.getId());

        partialUpdatedAttributeValue.creatAt(UPDATED_CREAT_AT).updateAt(UPDATED_UPDATE_AT).name(UPDATED_NAME);

        restAttributeValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttributeValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAttributeValue))
            )
            .andExpect(status().isOk());

        // Validate the AttributeValue in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAttributeValueUpdatableFieldsEquals(partialUpdatedAttributeValue, getPersistedAttributeValue(partialUpdatedAttributeValue));
    }

    @Test
    @Transactional
    void patchNonExistingAttributeValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attributeValue.setId(longCount.incrementAndGet());

        // Create the AttributeValue
        AttributeValueDTO attributeValueDTO = attributeValueMapper.toDto(attributeValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributeValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attributeValueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(attributeValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributeValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAttributeValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attributeValue.setId(longCount.incrementAndGet());

        // Create the AttributeValue
        AttributeValueDTO attributeValueDTO = attributeValueMapper.toDto(attributeValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(attributeValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributeValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAttributeValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attributeValue.setId(longCount.incrementAndGet());

        // Create the AttributeValue
        AttributeValueDTO attributeValueDTO = attributeValueMapper.toDto(attributeValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeValueMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(attributeValueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttributeValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAttributeValue() throws Exception {
        // Initialize the database
        insertedAttributeValue = attributeValueRepository.saveAndFlush(attributeValue);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the attributeValue
        restAttributeValueMockMvc
            .perform(delete(ENTITY_API_URL_ID, attributeValue.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return attributeValueRepository.count();
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

    protected AttributeValue getPersistedAttributeValue(AttributeValue attributeValue) {
        return attributeValueRepository.findById(attributeValue.getId()).orElseThrow();
    }

    protected void assertPersistedAttributeValueToMatchAllProperties(AttributeValue expectedAttributeValue) {
        assertAttributeValueAllPropertiesEquals(expectedAttributeValue, getPersistedAttributeValue(expectedAttributeValue));
    }

    protected void assertPersistedAttributeValueToMatchUpdatableProperties(AttributeValue expectedAttributeValue) {
        assertAttributeValueAllUpdatablePropertiesEquals(expectedAttributeValue, getPersistedAttributeValue(expectedAttributeValue));
    }
}
