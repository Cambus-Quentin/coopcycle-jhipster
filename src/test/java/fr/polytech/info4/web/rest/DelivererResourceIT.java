package fr.polytech.info4.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.polytech.info4.IntegrationTest;
import fr.polytech.info4.domain.Deliverer;
import fr.polytech.info4.domain.Delivery;
import fr.polytech.info4.domain.User;
import fr.polytech.info4.repository.DelivererRepository;
import fr.polytech.info4.service.criteria.DelivererCriteria;
import fr.polytech.info4.service.dto.DelivererDTO;
import fr.polytech.info4.service.mapper.DelivererMapper;
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
 * Integration tests for the {@link DelivererResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DelivererResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/deliverers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DelivererRepository delivererRepository;

    @Autowired
    private DelivererMapper delivererMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDelivererMockMvc;

    private Deliverer deliverer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deliverer createEntity(EntityManager em) {
        Deliverer deliverer = new Deliverer().name(DEFAULT_NAME).firstname(DEFAULT_FIRSTNAME);
        return deliverer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deliverer createUpdatedEntity(EntityManager em) {
        Deliverer deliverer = new Deliverer().name(UPDATED_NAME).firstname(UPDATED_FIRSTNAME);
        return deliverer;
    }

    @BeforeEach
    public void initTest() {
        deliverer = createEntity(em);
    }

    @Test
    @Transactional
    void createDeliverer() throws Exception {
        int databaseSizeBeforeCreate = delivererRepository.findAll().size();
        // Create the Deliverer
        DelivererDTO delivererDTO = delivererMapper.toDto(deliverer);
        restDelivererMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(delivererDTO)))
            .andExpect(status().isCreated());

        // Validate the Deliverer in the database
        List<Deliverer> delivererList = delivererRepository.findAll();
        assertThat(delivererList).hasSize(databaseSizeBeforeCreate + 1);
        Deliverer testDeliverer = delivererList.get(delivererList.size() - 1);
        assertThat(testDeliverer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDeliverer.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
    }

    @Test
    @Transactional
    void createDelivererWithExistingId() throws Exception {
        // Create the Deliverer with an existing ID
        deliverer.setId(1L);
        DelivererDTO delivererDTO = delivererMapper.toDto(deliverer);

        int databaseSizeBeforeCreate = delivererRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDelivererMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(delivererDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Deliverer in the database
        List<Deliverer> delivererList = delivererRepository.findAll();
        assertThat(delivererList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = delivererRepository.findAll().size();
        // set the field null
        deliverer.setName(null);

        // Create the Deliverer, which fails.
        DelivererDTO delivererDTO = delivererMapper.toDto(deliverer);

        restDelivererMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(delivererDTO)))
            .andExpect(status().isBadRequest());

        List<Deliverer> delivererList = delivererRepository.findAll();
        assertThat(delivererList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = delivererRepository.findAll().size();
        // set the field null
        deliverer.setFirstname(null);

        // Create the Deliverer, which fails.
        DelivererDTO delivererDTO = delivererMapper.toDto(deliverer);

        restDelivererMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(delivererDTO)))
            .andExpect(status().isBadRequest());

        List<Deliverer> delivererList = delivererRepository.findAll();
        assertThat(delivererList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeliverers() throws Exception {
        // Initialize the database
        delivererRepository.saveAndFlush(deliverer);

        // Get all the delivererList
        restDelivererMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliverer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)));
    }

    @Test
    @Transactional
    void getDeliverer() throws Exception {
        // Initialize the database
        delivererRepository.saveAndFlush(deliverer);

        // Get the deliverer
        restDelivererMockMvc
            .perform(get(ENTITY_API_URL_ID, deliverer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deliverer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME));
    }

    @Test
    @Transactional
    void getDeliverersByIdFiltering() throws Exception {
        // Initialize the database
        delivererRepository.saveAndFlush(deliverer);

        Long id = deliverer.getId();

        defaultDelivererShouldBeFound("id.equals=" + id);
        defaultDelivererShouldNotBeFound("id.notEquals=" + id);

        defaultDelivererShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDelivererShouldNotBeFound("id.greaterThan=" + id);

        defaultDelivererShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDelivererShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDeliverersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        delivererRepository.saveAndFlush(deliverer);

        // Get all the delivererList where name equals to DEFAULT_NAME
        defaultDelivererShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the delivererList where name equals to UPDATED_NAME
        defaultDelivererShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDeliverersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        delivererRepository.saveAndFlush(deliverer);

        // Get all the delivererList where name not equals to DEFAULT_NAME
        defaultDelivererShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the delivererList where name not equals to UPDATED_NAME
        defaultDelivererShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDeliverersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        delivererRepository.saveAndFlush(deliverer);

        // Get all the delivererList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDelivererShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the delivererList where name equals to UPDATED_NAME
        defaultDelivererShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDeliverersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        delivererRepository.saveAndFlush(deliverer);

        // Get all the delivererList where name is not null
        defaultDelivererShouldBeFound("name.specified=true");

        // Get all the delivererList where name is null
        defaultDelivererShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliverersByNameContainsSomething() throws Exception {
        // Initialize the database
        delivererRepository.saveAndFlush(deliverer);

        // Get all the delivererList where name contains DEFAULT_NAME
        defaultDelivererShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the delivererList where name contains UPDATED_NAME
        defaultDelivererShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDeliverersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        delivererRepository.saveAndFlush(deliverer);

        // Get all the delivererList where name does not contain DEFAULT_NAME
        defaultDelivererShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the delivererList where name does not contain UPDATED_NAME
        defaultDelivererShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDeliverersByFirstnameIsEqualToSomething() throws Exception {
        // Initialize the database
        delivererRepository.saveAndFlush(deliverer);

        // Get all the delivererList where firstname equals to DEFAULT_FIRSTNAME
        defaultDelivererShouldBeFound("firstname.equals=" + DEFAULT_FIRSTNAME);

        // Get all the delivererList where firstname equals to UPDATED_FIRSTNAME
        defaultDelivererShouldNotBeFound("firstname.equals=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllDeliverersByFirstnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        delivererRepository.saveAndFlush(deliverer);

        // Get all the delivererList where firstname not equals to DEFAULT_FIRSTNAME
        defaultDelivererShouldNotBeFound("firstname.notEquals=" + DEFAULT_FIRSTNAME);

        // Get all the delivererList where firstname not equals to UPDATED_FIRSTNAME
        defaultDelivererShouldBeFound("firstname.notEquals=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllDeliverersByFirstnameIsInShouldWork() throws Exception {
        // Initialize the database
        delivererRepository.saveAndFlush(deliverer);

        // Get all the delivererList where firstname in DEFAULT_FIRSTNAME or UPDATED_FIRSTNAME
        defaultDelivererShouldBeFound("firstname.in=" + DEFAULT_FIRSTNAME + "," + UPDATED_FIRSTNAME);

        // Get all the delivererList where firstname equals to UPDATED_FIRSTNAME
        defaultDelivererShouldNotBeFound("firstname.in=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllDeliverersByFirstnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        delivererRepository.saveAndFlush(deliverer);

        // Get all the delivererList where firstname is not null
        defaultDelivererShouldBeFound("firstname.specified=true");

        // Get all the delivererList where firstname is null
        defaultDelivererShouldNotBeFound("firstname.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliverersByFirstnameContainsSomething() throws Exception {
        // Initialize the database
        delivererRepository.saveAndFlush(deliverer);

        // Get all the delivererList where firstname contains DEFAULT_FIRSTNAME
        defaultDelivererShouldBeFound("firstname.contains=" + DEFAULT_FIRSTNAME);

        // Get all the delivererList where firstname contains UPDATED_FIRSTNAME
        defaultDelivererShouldNotBeFound("firstname.contains=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllDeliverersByFirstnameNotContainsSomething() throws Exception {
        // Initialize the database
        delivererRepository.saveAndFlush(deliverer);

        // Get all the delivererList where firstname does not contain DEFAULT_FIRSTNAME
        defaultDelivererShouldNotBeFound("firstname.doesNotContain=" + DEFAULT_FIRSTNAME);

        // Get all the delivererList where firstname does not contain UPDATED_FIRSTNAME
        defaultDelivererShouldBeFound("firstname.doesNotContain=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllDeliverersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        delivererRepository.saveAndFlush(deliverer);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        deliverer.setUser(user);
        delivererRepository.saveAndFlush(deliverer);
        Long userId = user.getId();

        // Get all the delivererList where user equals to userId
        defaultDelivererShouldBeFound("userId.equals=" + userId);

        // Get all the delivererList where user equals to (userId + 1)
        defaultDelivererShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllDeliverersByDeliveryIsEqualToSomething() throws Exception {
        // Initialize the database
        delivererRepository.saveAndFlush(deliverer);
        Delivery delivery = DeliveryResourceIT.createEntity(em);
        em.persist(delivery);
        em.flush();
        deliverer.addDelivery(delivery);
        delivererRepository.saveAndFlush(deliverer);
        Long deliveryId = delivery.getId();

        // Get all the delivererList where delivery equals to deliveryId
        defaultDelivererShouldBeFound("deliveryId.equals=" + deliveryId);

        // Get all the delivererList where delivery equals to (deliveryId + 1)
        defaultDelivererShouldNotBeFound("deliveryId.equals=" + (deliveryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDelivererShouldBeFound(String filter) throws Exception {
        restDelivererMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliverer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)));

        // Check, that the count call also returns 1
        restDelivererMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDelivererShouldNotBeFound(String filter) throws Exception {
        restDelivererMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDelivererMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDeliverer() throws Exception {
        // Get the deliverer
        restDelivererMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDeliverer() throws Exception {
        // Initialize the database
        delivererRepository.saveAndFlush(deliverer);

        int databaseSizeBeforeUpdate = delivererRepository.findAll().size();

        // Update the deliverer
        Deliverer updatedDeliverer = delivererRepository.findById(deliverer.getId()).get();
        // Disconnect from session so that the updates on updatedDeliverer are not directly saved in db
        em.detach(updatedDeliverer);
        updatedDeliverer.name(UPDATED_NAME).firstname(UPDATED_FIRSTNAME);
        DelivererDTO delivererDTO = delivererMapper.toDto(updatedDeliverer);

        restDelivererMockMvc
            .perform(
                put(ENTITY_API_URL_ID, delivererDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(delivererDTO))
            )
            .andExpect(status().isOk());

        // Validate the Deliverer in the database
        List<Deliverer> delivererList = delivererRepository.findAll();
        assertThat(delivererList).hasSize(databaseSizeBeforeUpdate);
        Deliverer testDeliverer = delivererList.get(delivererList.size() - 1);
        assertThat(testDeliverer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDeliverer.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void putNonExistingDeliverer() throws Exception {
        int databaseSizeBeforeUpdate = delivererRepository.findAll().size();
        deliverer.setId(count.incrementAndGet());

        // Create the Deliverer
        DelivererDTO delivererDTO = delivererMapper.toDto(deliverer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDelivererMockMvc
            .perform(
                put(ENTITY_API_URL_ID, delivererDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(delivererDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deliverer in the database
        List<Deliverer> delivererList = delivererRepository.findAll();
        assertThat(delivererList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeliverer() throws Exception {
        int databaseSizeBeforeUpdate = delivererRepository.findAll().size();
        deliverer.setId(count.incrementAndGet());

        // Create the Deliverer
        DelivererDTO delivererDTO = delivererMapper.toDto(deliverer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDelivererMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(delivererDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deliverer in the database
        List<Deliverer> delivererList = delivererRepository.findAll();
        assertThat(delivererList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeliverer() throws Exception {
        int databaseSizeBeforeUpdate = delivererRepository.findAll().size();
        deliverer.setId(count.incrementAndGet());

        // Create the Deliverer
        DelivererDTO delivererDTO = delivererMapper.toDto(deliverer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDelivererMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(delivererDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deliverer in the database
        List<Deliverer> delivererList = delivererRepository.findAll();
        assertThat(delivererList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDelivererWithPatch() throws Exception {
        // Initialize the database
        delivererRepository.saveAndFlush(deliverer);

        int databaseSizeBeforeUpdate = delivererRepository.findAll().size();

        // Update the deliverer using partial update
        Deliverer partialUpdatedDeliverer = new Deliverer();
        partialUpdatedDeliverer.setId(deliverer.getId());

        partialUpdatedDeliverer.name(UPDATED_NAME);

        restDelivererMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliverer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliverer))
            )
            .andExpect(status().isOk());

        // Validate the Deliverer in the database
        List<Deliverer> delivererList = delivererRepository.findAll();
        assertThat(delivererList).hasSize(databaseSizeBeforeUpdate);
        Deliverer testDeliverer = delivererList.get(delivererList.size() - 1);
        assertThat(testDeliverer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDeliverer.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
    }

    @Test
    @Transactional
    void fullUpdateDelivererWithPatch() throws Exception {
        // Initialize the database
        delivererRepository.saveAndFlush(deliverer);

        int databaseSizeBeforeUpdate = delivererRepository.findAll().size();

        // Update the deliverer using partial update
        Deliverer partialUpdatedDeliverer = new Deliverer();
        partialUpdatedDeliverer.setId(deliverer.getId());

        partialUpdatedDeliverer.name(UPDATED_NAME).firstname(UPDATED_FIRSTNAME);

        restDelivererMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliverer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliverer))
            )
            .andExpect(status().isOk());

        // Validate the Deliverer in the database
        List<Deliverer> delivererList = delivererRepository.findAll();
        assertThat(delivererList).hasSize(databaseSizeBeforeUpdate);
        Deliverer testDeliverer = delivererList.get(delivererList.size() - 1);
        assertThat(testDeliverer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDeliverer.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void patchNonExistingDeliverer() throws Exception {
        int databaseSizeBeforeUpdate = delivererRepository.findAll().size();
        deliverer.setId(count.incrementAndGet());

        // Create the Deliverer
        DelivererDTO delivererDTO = delivererMapper.toDto(deliverer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDelivererMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, delivererDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(delivererDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deliverer in the database
        List<Deliverer> delivererList = delivererRepository.findAll();
        assertThat(delivererList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeliverer() throws Exception {
        int databaseSizeBeforeUpdate = delivererRepository.findAll().size();
        deliverer.setId(count.incrementAndGet());

        // Create the Deliverer
        DelivererDTO delivererDTO = delivererMapper.toDto(deliverer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDelivererMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(delivererDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deliverer in the database
        List<Deliverer> delivererList = delivererRepository.findAll();
        assertThat(delivererList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeliverer() throws Exception {
        int databaseSizeBeforeUpdate = delivererRepository.findAll().size();
        deliverer.setId(count.incrementAndGet());

        // Create the Deliverer
        DelivererDTO delivererDTO = delivererMapper.toDto(deliverer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDelivererMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(delivererDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deliverer in the database
        List<Deliverer> delivererList = delivererRepository.findAll();
        assertThat(delivererList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeliverer() throws Exception {
        // Initialize the database
        delivererRepository.saveAndFlush(deliverer);

        int databaseSizeBeforeDelete = delivererRepository.findAll().size();

        // Delete the deliverer
        restDelivererMockMvc
            .perform(delete(ENTITY_API_URL_ID, deliverer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Deliverer> delivererList = delivererRepository.findAll();
        assertThat(delivererList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
