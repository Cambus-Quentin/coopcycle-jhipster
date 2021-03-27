package fr.polytech.info4.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.polytech.info4.IntegrationTest;
import fr.polytech.info4.domain.LocalCooperative;
import fr.polytech.info4.domain.NationalCooperative;
import fr.polytech.info4.domain.Restaurant;
import fr.polytech.info4.repository.LocalCooperativeRepository;
import fr.polytech.info4.service.criteria.LocalCooperativeCriteria;
import fr.polytech.info4.service.dto.LocalCooperativeDTO;
import fr.polytech.info4.service.mapper.LocalCooperativeMapper;
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
 * Integration tests for the {@link LocalCooperativeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocalCooperativeResourceIT {

    private static final String DEFAULT_GEO_ZONE = "AAAAAAAAAA";
    private static final String UPDATED_GEO_ZONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/local-cooperatives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocalCooperativeRepository localCooperativeRepository;

    @Autowired
    private LocalCooperativeMapper localCooperativeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocalCooperativeMockMvc;

    private LocalCooperative localCooperative;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocalCooperative createEntity(EntityManager em) {
        LocalCooperative localCooperative = new LocalCooperative().geoZone(DEFAULT_GEO_ZONE);
        return localCooperative;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocalCooperative createUpdatedEntity(EntityManager em) {
        LocalCooperative localCooperative = new LocalCooperative().geoZone(UPDATED_GEO_ZONE);
        return localCooperative;
    }

    @BeforeEach
    public void initTest() {
        localCooperative = createEntity(em);
    }

    @Test
    @Transactional
    void createLocalCooperative() throws Exception {
        int databaseSizeBeforeCreate = localCooperativeRepository.findAll().size();
        // Create the LocalCooperative
        LocalCooperativeDTO localCooperativeDTO = localCooperativeMapper.toDto(localCooperative);
        restLocalCooperativeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localCooperativeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LocalCooperative in the database
        List<LocalCooperative> localCooperativeList = localCooperativeRepository.findAll();
        assertThat(localCooperativeList).hasSize(databaseSizeBeforeCreate + 1);
        LocalCooperative testLocalCooperative = localCooperativeList.get(localCooperativeList.size() - 1);
        assertThat(testLocalCooperative.getGeoZone()).isEqualTo(DEFAULT_GEO_ZONE);
    }

    @Test
    @Transactional
    void createLocalCooperativeWithExistingId() throws Exception {
        // Create the LocalCooperative with an existing ID
        localCooperative.setId(1L);
        LocalCooperativeDTO localCooperativeDTO = localCooperativeMapper.toDto(localCooperative);

        int databaseSizeBeforeCreate = localCooperativeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocalCooperativeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localCooperativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocalCooperative in the database
        List<LocalCooperative> localCooperativeList = localCooperativeRepository.findAll();
        assertThat(localCooperativeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkGeoZoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = localCooperativeRepository.findAll().size();
        // set the field null
        localCooperative.setGeoZone(null);

        // Create the LocalCooperative, which fails.
        LocalCooperativeDTO localCooperativeDTO = localCooperativeMapper.toDto(localCooperative);

        restLocalCooperativeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localCooperativeDTO))
            )
            .andExpect(status().isBadRequest());

        List<LocalCooperative> localCooperativeList = localCooperativeRepository.findAll();
        assertThat(localCooperativeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLocalCooperatives() throws Exception {
        // Initialize the database
        localCooperativeRepository.saveAndFlush(localCooperative);

        // Get all the localCooperativeList
        restLocalCooperativeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(localCooperative.getId().intValue())))
            .andExpect(jsonPath("$.[*].geoZone").value(hasItem(DEFAULT_GEO_ZONE)));
    }

    @Test
    @Transactional
    void getLocalCooperative() throws Exception {
        // Initialize the database
        localCooperativeRepository.saveAndFlush(localCooperative);

        // Get the localCooperative
        restLocalCooperativeMockMvc
            .perform(get(ENTITY_API_URL_ID, localCooperative.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(localCooperative.getId().intValue()))
            .andExpect(jsonPath("$.geoZone").value(DEFAULT_GEO_ZONE));
    }

    @Test
    @Transactional
    void getLocalCooperativesByIdFiltering() throws Exception {
        // Initialize the database
        localCooperativeRepository.saveAndFlush(localCooperative);

        Long id = localCooperative.getId();

        defaultLocalCooperativeShouldBeFound("id.equals=" + id);
        defaultLocalCooperativeShouldNotBeFound("id.notEquals=" + id);

        defaultLocalCooperativeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLocalCooperativeShouldNotBeFound("id.greaterThan=" + id);

        defaultLocalCooperativeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLocalCooperativeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLocalCooperativesByGeoZoneIsEqualToSomething() throws Exception {
        // Initialize the database
        localCooperativeRepository.saveAndFlush(localCooperative);

        // Get all the localCooperativeList where geoZone equals to DEFAULT_GEO_ZONE
        defaultLocalCooperativeShouldBeFound("geoZone.equals=" + DEFAULT_GEO_ZONE);

        // Get all the localCooperativeList where geoZone equals to UPDATED_GEO_ZONE
        defaultLocalCooperativeShouldNotBeFound("geoZone.equals=" + UPDATED_GEO_ZONE);
    }

    @Test
    @Transactional
    void getAllLocalCooperativesByGeoZoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        localCooperativeRepository.saveAndFlush(localCooperative);

        // Get all the localCooperativeList where geoZone not equals to DEFAULT_GEO_ZONE
        defaultLocalCooperativeShouldNotBeFound("geoZone.notEquals=" + DEFAULT_GEO_ZONE);

        // Get all the localCooperativeList where geoZone not equals to UPDATED_GEO_ZONE
        defaultLocalCooperativeShouldBeFound("geoZone.notEquals=" + UPDATED_GEO_ZONE);
    }

    @Test
    @Transactional
    void getAllLocalCooperativesByGeoZoneIsInShouldWork() throws Exception {
        // Initialize the database
        localCooperativeRepository.saveAndFlush(localCooperative);

        // Get all the localCooperativeList where geoZone in DEFAULT_GEO_ZONE or UPDATED_GEO_ZONE
        defaultLocalCooperativeShouldBeFound("geoZone.in=" + DEFAULT_GEO_ZONE + "," + UPDATED_GEO_ZONE);

        // Get all the localCooperativeList where geoZone equals to UPDATED_GEO_ZONE
        defaultLocalCooperativeShouldNotBeFound("geoZone.in=" + UPDATED_GEO_ZONE);
    }

    @Test
    @Transactional
    void getAllLocalCooperativesByGeoZoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        localCooperativeRepository.saveAndFlush(localCooperative);

        // Get all the localCooperativeList where geoZone is not null
        defaultLocalCooperativeShouldBeFound("geoZone.specified=true");

        // Get all the localCooperativeList where geoZone is null
        defaultLocalCooperativeShouldNotBeFound("geoZone.specified=false");
    }

    @Test
    @Transactional
    void getAllLocalCooperativesByGeoZoneContainsSomething() throws Exception {
        // Initialize the database
        localCooperativeRepository.saveAndFlush(localCooperative);

        // Get all the localCooperativeList where geoZone contains DEFAULT_GEO_ZONE
        defaultLocalCooperativeShouldBeFound("geoZone.contains=" + DEFAULT_GEO_ZONE);

        // Get all the localCooperativeList where geoZone contains UPDATED_GEO_ZONE
        defaultLocalCooperativeShouldNotBeFound("geoZone.contains=" + UPDATED_GEO_ZONE);
    }

    @Test
    @Transactional
    void getAllLocalCooperativesByGeoZoneNotContainsSomething() throws Exception {
        // Initialize the database
        localCooperativeRepository.saveAndFlush(localCooperative);

        // Get all the localCooperativeList where geoZone does not contain DEFAULT_GEO_ZONE
        defaultLocalCooperativeShouldNotBeFound("geoZone.doesNotContain=" + DEFAULT_GEO_ZONE);

        // Get all the localCooperativeList where geoZone does not contain UPDATED_GEO_ZONE
        defaultLocalCooperativeShouldBeFound("geoZone.doesNotContain=" + UPDATED_GEO_ZONE);
    }

    @Test
    @Transactional
    void getAllLocalCooperativesByNationalCooperativeIsEqualToSomething() throws Exception {
        // Initialize the database
        localCooperativeRepository.saveAndFlush(localCooperative);
        NationalCooperative nationalCooperative = NationalCooperativeResourceIT.createEntity(em);
        em.persist(nationalCooperative);
        em.flush();
        localCooperative.setNationalCooperative(nationalCooperative);
        localCooperativeRepository.saveAndFlush(localCooperative);
        Long nationalCooperativeId = nationalCooperative.getId();

        // Get all the localCooperativeList where nationalCooperative equals to nationalCooperativeId
        defaultLocalCooperativeShouldBeFound("nationalCooperativeId.equals=" + nationalCooperativeId);

        // Get all the localCooperativeList where nationalCooperative equals to (nationalCooperativeId + 1)
        defaultLocalCooperativeShouldNotBeFound("nationalCooperativeId.equals=" + (nationalCooperativeId + 1));
    }

    @Test
    @Transactional
    void getAllLocalCooperativesByRestaurantIsEqualToSomething() throws Exception {
        // Initialize the database
        localCooperativeRepository.saveAndFlush(localCooperative);
        Restaurant restaurant = RestaurantResourceIT.createEntity(em);
        em.persist(restaurant);
        em.flush();
        localCooperative.addRestaurant(restaurant);
        localCooperativeRepository.saveAndFlush(localCooperative);
        Long restaurantId = restaurant.getId();

        // Get all the localCooperativeList where restaurant equals to restaurantId
        defaultLocalCooperativeShouldBeFound("restaurantId.equals=" + restaurantId);

        // Get all the localCooperativeList where restaurant equals to (restaurantId + 1)
        defaultLocalCooperativeShouldNotBeFound("restaurantId.equals=" + (restaurantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLocalCooperativeShouldBeFound(String filter) throws Exception {
        restLocalCooperativeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(localCooperative.getId().intValue())))
            .andExpect(jsonPath("$.[*].geoZone").value(hasItem(DEFAULT_GEO_ZONE)));

        // Check, that the count call also returns 1
        restLocalCooperativeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLocalCooperativeShouldNotBeFound(String filter) throws Exception {
        restLocalCooperativeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLocalCooperativeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLocalCooperative() throws Exception {
        // Get the localCooperative
        restLocalCooperativeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLocalCooperative() throws Exception {
        // Initialize the database
        localCooperativeRepository.saveAndFlush(localCooperative);

        int databaseSizeBeforeUpdate = localCooperativeRepository.findAll().size();

        // Update the localCooperative
        LocalCooperative updatedLocalCooperative = localCooperativeRepository.findById(localCooperative.getId()).get();
        // Disconnect from session so that the updates on updatedLocalCooperative are not directly saved in db
        em.detach(updatedLocalCooperative);
        updatedLocalCooperative.geoZone(UPDATED_GEO_ZONE);
        LocalCooperativeDTO localCooperativeDTO = localCooperativeMapper.toDto(updatedLocalCooperative);

        restLocalCooperativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, localCooperativeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localCooperativeDTO))
            )
            .andExpect(status().isOk());

        // Validate the LocalCooperative in the database
        List<LocalCooperative> localCooperativeList = localCooperativeRepository.findAll();
        assertThat(localCooperativeList).hasSize(databaseSizeBeforeUpdate);
        LocalCooperative testLocalCooperative = localCooperativeList.get(localCooperativeList.size() - 1);
        assertThat(testLocalCooperative.getGeoZone()).isEqualTo(UPDATED_GEO_ZONE);
    }

    @Test
    @Transactional
    void putNonExistingLocalCooperative() throws Exception {
        int databaseSizeBeforeUpdate = localCooperativeRepository.findAll().size();
        localCooperative.setId(count.incrementAndGet());

        // Create the LocalCooperative
        LocalCooperativeDTO localCooperativeDTO = localCooperativeMapper.toDto(localCooperative);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalCooperativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, localCooperativeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localCooperativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocalCooperative in the database
        List<LocalCooperative> localCooperativeList = localCooperativeRepository.findAll();
        assertThat(localCooperativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocalCooperative() throws Exception {
        int databaseSizeBeforeUpdate = localCooperativeRepository.findAll().size();
        localCooperative.setId(count.incrementAndGet());

        // Create the LocalCooperative
        LocalCooperativeDTO localCooperativeDTO = localCooperativeMapper.toDto(localCooperative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalCooperativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localCooperativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocalCooperative in the database
        List<LocalCooperative> localCooperativeList = localCooperativeRepository.findAll();
        assertThat(localCooperativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocalCooperative() throws Exception {
        int databaseSizeBeforeUpdate = localCooperativeRepository.findAll().size();
        localCooperative.setId(count.incrementAndGet());

        // Create the LocalCooperative
        LocalCooperativeDTO localCooperativeDTO = localCooperativeMapper.toDto(localCooperative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalCooperativeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localCooperativeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocalCooperative in the database
        List<LocalCooperative> localCooperativeList = localCooperativeRepository.findAll();
        assertThat(localCooperativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocalCooperativeWithPatch() throws Exception {
        // Initialize the database
        localCooperativeRepository.saveAndFlush(localCooperative);

        int databaseSizeBeforeUpdate = localCooperativeRepository.findAll().size();

        // Update the localCooperative using partial update
        LocalCooperative partialUpdatedLocalCooperative = new LocalCooperative();
        partialUpdatedLocalCooperative.setId(localCooperative.getId());

        restLocalCooperativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocalCooperative.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocalCooperative))
            )
            .andExpect(status().isOk());

        // Validate the LocalCooperative in the database
        List<LocalCooperative> localCooperativeList = localCooperativeRepository.findAll();
        assertThat(localCooperativeList).hasSize(databaseSizeBeforeUpdate);
        LocalCooperative testLocalCooperative = localCooperativeList.get(localCooperativeList.size() - 1);
        assertThat(testLocalCooperative.getGeoZone()).isEqualTo(DEFAULT_GEO_ZONE);
    }

    @Test
    @Transactional
    void fullUpdateLocalCooperativeWithPatch() throws Exception {
        // Initialize the database
        localCooperativeRepository.saveAndFlush(localCooperative);

        int databaseSizeBeforeUpdate = localCooperativeRepository.findAll().size();

        // Update the localCooperative using partial update
        LocalCooperative partialUpdatedLocalCooperative = new LocalCooperative();
        partialUpdatedLocalCooperative.setId(localCooperative.getId());

        partialUpdatedLocalCooperative.geoZone(UPDATED_GEO_ZONE);

        restLocalCooperativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocalCooperative.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocalCooperative))
            )
            .andExpect(status().isOk());

        // Validate the LocalCooperative in the database
        List<LocalCooperative> localCooperativeList = localCooperativeRepository.findAll();
        assertThat(localCooperativeList).hasSize(databaseSizeBeforeUpdate);
        LocalCooperative testLocalCooperative = localCooperativeList.get(localCooperativeList.size() - 1);
        assertThat(testLocalCooperative.getGeoZone()).isEqualTo(UPDATED_GEO_ZONE);
    }

    @Test
    @Transactional
    void patchNonExistingLocalCooperative() throws Exception {
        int databaseSizeBeforeUpdate = localCooperativeRepository.findAll().size();
        localCooperative.setId(count.incrementAndGet());

        // Create the LocalCooperative
        LocalCooperativeDTO localCooperativeDTO = localCooperativeMapper.toDto(localCooperative);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalCooperativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, localCooperativeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(localCooperativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocalCooperative in the database
        List<LocalCooperative> localCooperativeList = localCooperativeRepository.findAll();
        assertThat(localCooperativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocalCooperative() throws Exception {
        int databaseSizeBeforeUpdate = localCooperativeRepository.findAll().size();
        localCooperative.setId(count.incrementAndGet());

        // Create the LocalCooperative
        LocalCooperativeDTO localCooperativeDTO = localCooperativeMapper.toDto(localCooperative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalCooperativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(localCooperativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocalCooperative in the database
        List<LocalCooperative> localCooperativeList = localCooperativeRepository.findAll();
        assertThat(localCooperativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocalCooperative() throws Exception {
        int databaseSizeBeforeUpdate = localCooperativeRepository.findAll().size();
        localCooperative.setId(count.incrementAndGet());

        // Create the LocalCooperative
        LocalCooperativeDTO localCooperativeDTO = localCooperativeMapper.toDto(localCooperative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalCooperativeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(localCooperativeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocalCooperative in the database
        List<LocalCooperative> localCooperativeList = localCooperativeRepository.findAll();
        assertThat(localCooperativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocalCooperative() throws Exception {
        // Initialize the database
        localCooperativeRepository.saveAndFlush(localCooperative);

        int databaseSizeBeforeDelete = localCooperativeRepository.findAll().size();

        // Delete the localCooperative
        restLocalCooperativeMockMvc
            .perform(delete(ENTITY_API_URL_ID, localCooperative.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LocalCooperative> localCooperativeList = localCooperativeRepository.findAll();
        assertThat(localCooperativeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
