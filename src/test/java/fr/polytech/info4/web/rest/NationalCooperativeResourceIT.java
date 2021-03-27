package fr.polytech.info4.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.polytech.info4.IntegrationTest;
import fr.polytech.info4.domain.LocalCooperative;
import fr.polytech.info4.domain.NationalCooperative;
import fr.polytech.info4.repository.NationalCooperativeRepository;
import fr.polytech.info4.service.criteria.NationalCooperativeCriteria;
import fr.polytech.info4.service.dto.NationalCooperativeDTO;
import fr.polytech.info4.service.mapper.NationalCooperativeMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link NationalCooperativeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NationalCooperativeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/national-cooperatives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NationalCooperativeRepository nationalCooperativeRepository;

    @Autowired
    private NationalCooperativeMapper nationalCooperativeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNationalCooperativeMockMvc;

    private NationalCooperative nationalCooperative;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NationalCooperative createEntity(EntityManager em) {
        NationalCooperative nationalCooperative = new NationalCooperative().name(DEFAULT_NAME);
        return nationalCooperative;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NationalCooperative createUpdatedEntity(EntityManager em) {
        NationalCooperative nationalCooperative = new NationalCooperative().name(UPDATED_NAME);
        return nationalCooperative;
    }

    @BeforeEach
    public void initTest() {
        nationalCooperative = createEntity(em);
    }

    @Test
    @Transactional
    void createNationalCooperative() throws Exception {
        int databaseSizeBeforeCreate = nationalCooperativeRepository.findAll().size();
        // Create the NationalCooperative
        NationalCooperativeDTO nationalCooperativeDTO = nationalCooperativeMapper.toDto(nationalCooperative);
        restNationalCooperativeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nationalCooperativeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the NationalCooperative in the database
        List<NationalCooperative> nationalCooperativeList = nationalCooperativeRepository.findAll();
        assertThat(nationalCooperativeList).hasSize(databaseSizeBeforeCreate + 1);
        NationalCooperative testNationalCooperative = nationalCooperativeList.get(nationalCooperativeList.size() - 1);
        assertThat(testNationalCooperative.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createNationalCooperativeWithExistingId() throws Exception {
        // Create the NationalCooperative with an existing ID
        nationalCooperative.setId(1L);
        NationalCooperativeDTO nationalCooperativeDTO = nationalCooperativeMapper.toDto(nationalCooperative);

        int databaseSizeBeforeCreate = nationalCooperativeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNationalCooperativeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nationalCooperativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NationalCooperative in the database
        List<NationalCooperative> nationalCooperativeList = nationalCooperativeRepository.findAll();
        assertThat(nationalCooperativeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = nationalCooperativeRepository.findAll().size();
        // set the field null
        nationalCooperative.setName(null);

        // Create the NationalCooperative, which fails.
        NationalCooperativeDTO nationalCooperativeDTO = nationalCooperativeMapper.toDto(nationalCooperative);

        restNationalCooperativeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nationalCooperativeDTO))
            )
            .andExpect(status().isBadRequest());

        List<NationalCooperative> nationalCooperativeList = nationalCooperativeRepository.findAll();
        assertThat(nationalCooperativeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNationalCooperatives() throws Exception {
        // Initialize the database
        nationalCooperativeRepository.saveAndFlush(nationalCooperative);

        // Get all the nationalCooperativeList
        restNationalCooperativeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nationalCooperative.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getNationalCooperative() throws Exception {
        // Initialize the database
        nationalCooperativeRepository.saveAndFlush(nationalCooperative);

        // Get the nationalCooperative
        restNationalCooperativeMockMvc
            .perform(get(ENTITY_API_URL_ID, nationalCooperative.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nationalCooperative.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNationalCooperativesByIdFiltering() throws Exception {
        // Initialize the database
        nationalCooperativeRepository.saveAndFlush(nationalCooperative);

        Long id = nationalCooperative.getId();

        defaultNationalCooperativeShouldBeFound("id.equals=" + id);
        defaultNationalCooperativeShouldNotBeFound("id.notEquals=" + id);

        defaultNationalCooperativeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNationalCooperativeShouldNotBeFound("id.greaterThan=" + id);

        defaultNationalCooperativeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNationalCooperativeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNationalCooperativesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        nationalCooperativeRepository.saveAndFlush(nationalCooperative);

        // Get all the nationalCooperativeList where name equals to DEFAULT_NAME
        defaultNationalCooperativeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the nationalCooperativeList where name equals to UPDATED_NAME
        defaultNationalCooperativeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNationalCooperativesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nationalCooperativeRepository.saveAndFlush(nationalCooperative);

        // Get all the nationalCooperativeList where name not equals to DEFAULT_NAME
        defaultNationalCooperativeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the nationalCooperativeList where name not equals to UPDATED_NAME
        defaultNationalCooperativeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNationalCooperativesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        nationalCooperativeRepository.saveAndFlush(nationalCooperative);

        // Get all the nationalCooperativeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultNationalCooperativeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the nationalCooperativeList where name equals to UPDATED_NAME
        defaultNationalCooperativeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNationalCooperativesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        nationalCooperativeRepository.saveAndFlush(nationalCooperative);

        // Get all the nationalCooperativeList where name is not null
        defaultNationalCooperativeShouldBeFound("name.specified=true");

        // Get all the nationalCooperativeList where name is null
        defaultNationalCooperativeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllNationalCooperativesByNameContainsSomething() throws Exception {
        // Initialize the database
        nationalCooperativeRepository.saveAndFlush(nationalCooperative);

        // Get all the nationalCooperativeList where name contains DEFAULT_NAME
        defaultNationalCooperativeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the nationalCooperativeList where name contains UPDATED_NAME
        defaultNationalCooperativeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNationalCooperativesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        nationalCooperativeRepository.saveAndFlush(nationalCooperative);

        // Get all the nationalCooperativeList where name does not contain DEFAULT_NAME
        defaultNationalCooperativeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the nationalCooperativeList where name does not contain UPDATED_NAME
        defaultNationalCooperativeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNationalCooperativesByLocalCooperativeIsEqualToSomething() throws Exception {
        // Initialize the database
        nationalCooperativeRepository.saveAndFlush(nationalCooperative);
        LocalCooperative localCooperative = LocalCooperativeResourceIT.createEntity(em);
        em.persist(localCooperative);
        em.flush();
        nationalCooperative.addLocalCooperative(localCooperative);
        nationalCooperativeRepository.saveAndFlush(nationalCooperative);
        Long localCooperativeId = localCooperative.getId();

        // Get all the nationalCooperativeList where localCooperative equals to localCooperativeId
        defaultNationalCooperativeShouldBeFound("localCooperativeId.equals=" + localCooperativeId);

        // Get all the nationalCooperativeList where localCooperative equals to (localCooperativeId + 1)
        defaultNationalCooperativeShouldNotBeFound("localCooperativeId.equals=" + (localCooperativeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNationalCooperativeShouldBeFound(String filter) throws Exception {
        restNationalCooperativeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nationalCooperative.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restNationalCooperativeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNationalCooperativeShouldNotBeFound(String filter) throws Exception {
        restNationalCooperativeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNationalCooperativeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNationalCooperative() throws Exception {
        // Get the nationalCooperative
        restNationalCooperativeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNationalCooperative() throws Exception {
        // Initialize the database
        nationalCooperativeRepository.saveAndFlush(nationalCooperative);

        int databaseSizeBeforeUpdate = nationalCooperativeRepository.findAll().size();

        // Update the nationalCooperative
        NationalCooperative updatedNationalCooperative = nationalCooperativeRepository.findById(nationalCooperative.getId()).get();
        // Disconnect from session so that the updates on updatedNationalCooperative are not directly saved in db
        em.detach(updatedNationalCooperative);
        updatedNationalCooperative.name(UPDATED_NAME);
        NationalCooperativeDTO nationalCooperativeDTO = nationalCooperativeMapper.toDto(updatedNationalCooperative);

        restNationalCooperativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nationalCooperativeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nationalCooperativeDTO))
            )
            .andExpect(status().isOk());

        // Validate the NationalCooperative in the database
        List<NationalCooperative> nationalCooperativeList = nationalCooperativeRepository.findAll();
        assertThat(nationalCooperativeList).hasSize(databaseSizeBeforeUpdate);
        NationalCooperative testNationalCooperative = nationalCooperativeList.get(nationalCooperativeList.size() - 1);
        assertThat(testNationalCooperative.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingNationalCooperative() throws Exception {
        int databaseSizeBeforeUpdate = nationalCooperativeRepository.findAll().size();
        nationalCooperative.setId(count.incrementAndGet());

        // Create the NationalCooperative
        NationalCooperativeDTO nationalCooperativeDTO = nationalCooperativeMapper.toDto(nationalCooperative);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNationalCooperativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nationalCooperativeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nationalCooperativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NationalCooperative in the database
        List<NationalCooperative> nationalCooperativeList = nationalCooperativeRepository.findAll();
        assertThat(nationalCooperativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNationalCooperative() throws Exception {
        int databaseSizeBeforeUpdate = nationalCooperativeRepository.findAll().size();
        nationalCooperative.setId(count.incrementAndGet());

        // Create the NationalCooperative
        NationalCooperativeDTO nationalCooperativeDTO = nationalCooperativeMapper.toDto(nationalCooperative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNationalCooperativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nationalCooperativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NationalCooperative in the database
        List<NationalCooperative> nationalCooperativeList = nationalCooperativeRepository.findAll();
        assertThat(nationalCooperativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNationalCooperative() throws Exception {
        int databaseSizeBeforeUpdate = nationalCooperativeRepository.findAll().size();
        nationalCooperative.setId(count.incrementAndGet());

        // Create the NationalCooperative
        NationalCooperativeDTO nationalCooperativeDTO = nationalCooperativeMapper.toDto(nationalCooperative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNationalCooperativeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nationalCooperativeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NationalCooperative in the database
        List<NationalCooperative> nationalCooperativeList = nationalCooperativeRepository.findAll();
        assertThat(nationalCooperativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNationalCooperativeWithPatch() throws Exception {
        // Initialize the database
        nationalCooperativeRepository.saveAndFlush(nationalCooperative);

        int databaseSizeBeforeUpdate = nationalCooperativeRepository.findAll().size();

        // Update the nationalCooperative using partial update
        NationalCooperative partialUpdatedNationalCooperative = new NationalCooperative();
        partialUpdatedNationalCooperative.setId(nationalCooperative.getId());

        restNationalCooperativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNationalCooperative.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNationalCooperative))
            )
            .andExpect(status().isOk());

        // Validate the NationalCooperative in the database
        List<NationalCooperative> nationalCooperativeList = nationalCooperativeRepository.findAll();
        assertThat(nationalCooperativeList).hasSize(databaseSizeBeforeUpdate);
        NationalCooperative testNationalCooperative = nationalCooperativeList.get(nationalCooperativeList.size() - 1);
        assertThat(testNationalCooperative.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateNationalCooperativeWithPatch() throws Exception {
        // Initialize the database
        nationalCooperativeRepository.saveAndFlush(nationalCooperative);

        int databaseSizeBeforeUpdate = nationalCooperativeRepository.findAll().size();

        // Update the nationalCooperative using partial update
        NationalCooperative partialUpdatedNationalCooperative = new NationalCooperative();
        partialUpdatedNationalCooperative.setId(nationalCooperative.getId());

        partialUpdatedNationalCooperative.name(UPDATED_NAME);

        restNationalCooperativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNationalCooperative.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNationalCooperative))
            )
            .andExpect(status().isOk());

        // Validate the NationalCooperative in the database
        List<NationalCooperative> nationalCooperativeList = nationalCooperativeRepository.findAll();
        assertThat(nationalCooperativeList).hasSize(databaseSizeBeforeUpdate);
        NationalCooperative testNationalCooperative = nationalCooperativeList.get(nationalCooperativeList.size() - 1);
        assertThat(testNationalCooperative.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingNationalCooperative() throws Exception {
        int databaseSizeBeforeUpdate = nationalCooperativeRepository.findAll().size();
        nationalCooperative.setId(count.incrementAndGet());

        // Create the NationalCooperative
        NationalCooperativeDTO nationalCooperativeDTO = nationalCooperativeMapper.toDto(nationalCooperative);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNationalCooperativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nationalCooperativeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nationalCooperativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NationalCooperative in the database
        List<NationalCooperative> nationalCooperativeList = nationalCooperativeRepository.findAll();
        assertThat(nationalCooperativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNationalCooperative() throws Exception {
        int databaseSizeBeforeUpdate = nationalCooperativeRepository.findAll().size();
        nationalCooperative.setId(count.incrementAndGet());

        // Create the NationalCooperative
        NationalCooperativeDTO nationalCooperativeDTO = nationalCooperativeMapper.toDto(nationalCooperative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNationalCooperativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nationalCooperativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NationalCooperative in the database
        List<NationalCooperative> nationalCooperativeList = nationalCooperativeRepository.findAll();
        assertThat(nationalCooperativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNationalCooperative() throws Exception {
        int databaseSizeBeforeUpdate = nationalCooperativeRepository.findAll().size();
        nationalCooperative.setId(count.incrementAndGet());

        // Create the NationalCooperative
        NationalCooperativeDTO nationalCooperativeDTO = nationalCooperativeMapper.toDto(nationalCooperative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNationalCooperativeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nationalCooperativeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NationalCooperative in the database
        List<NationalCooperative> nationalCooperativeList = nationalCooperativeRepository.findAll();
        assertThat(nationalCooperativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNationalCooperative() throws Exception {
        // Initialize the database
        nationalCooperativeRepository.saveAndFlush(nationalCooperative);

        int databaseSizeBeforeDelete = nationalCooperativeRepository.findAll().size();

        // Delete the nationalCooperative
        restNationalCooperativeMockMvc
            .perform(delete(ENTITY_API_URL_ID, nationalCooperative.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NationalCooperative> nationalCooperativeList = nationalCooperativeRepository.findAll();
        assertThat(nationalCooperativeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
