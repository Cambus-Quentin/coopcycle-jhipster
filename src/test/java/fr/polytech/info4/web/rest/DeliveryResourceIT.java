package fr.polytech.info4.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.polytech.info4.IntegrationTest;
import fr.polytech.info4.domain.Command;
import fr.polytech.info4.domain.Deliverer;
import fr.polytech.info4.domain.Delivery;
import fr.polytech.info4.repository.DeliveryRepository;
import fr.polytech.info4.service.criteria.DeliveryCriteria;
import fr.polytech.info4.service.dto.DeliveryDTO;
import fr.polytech.info4.service.mapper.DeliveryMapper;
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
 * Integration tests for the {@link DeliveryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeliveryResourceIT {

    private static final String DEFAULT_DELIVERY_ADDR = "AAAAAAAAAA";
    private static final String UPDATED_DELIVERY_ADDR = "BBBBBBBBBB";

    private static final Integer DEFAULT_DISTANCE = 1;
    private static final Integer UPDATED_DISTANCE = 2;
    private static final Integer SMALLER_DISTANCE = 1 - 1;

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;
    private static final Float SMALLER_PRICE = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/deliveries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private DeliveryMapper deliveryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeliveryMockMvc;

    private Delivery delivery;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Delivery createEntity(EntityManager em) {
        Delivery delivery = new Delivery().deliveryAddr(DEFAULT_DELIVERY_ADDR).distance(DEFAULT_DISTANCE).price(DEFAULT_PRICE);
        return delivery;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Delivery createUpdatedEntity(EntityManager em) {
        Delivery delivery = new Delivery().deliveryAddr(UPDATED_DELIVERY_ADDR).distance(UPDATED_DISTANCE).price(UPDATED_PRICE);
        return delivery;
    }

    @BeforeEach
    public void initTest() {
        delivery = createEntity(em);
    }

    @Test
    @Transactional
    void createDelivery() throws Exception {
        int databaseSizeBeforeCreate = deliveryRepository.findAll().size();
        // Create the Delivery
        DeliveryDTO deliveryDTO = deliveryMapper.toDto(delivery);
        restDeliveryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryDTO)))
            .andExpect(status().isCreated());

        // Validate the Delivery in the database
        List<Delivery> deliveryList = deliveryRepository.findAll();
        assertThat(deliveryList).hasSize(databaseSizeBeforeCreate + 1);
        Delivery testDelivery = deliveryList.get(deliveryList.size() - 1);
        assertThat(testDelivery.getDeliveryAddr()).isEqualTo(DEFAULT_DELIVERY_ADDR);
        assertThat(testDelivery.getDistance()).isEqualTo(DEFAULT_DISTANCE);
        assertThat(testDelivery.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createDeliveryWithExistingId() throws Exception {
        // Create the Delivery with an existing ID
        delivery.setId(1L);
        DeliveryDTO deliveryDTO = deliveryMapper.toDto(delivery);

        int databaseSizeBeforeCreate = deliveryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeliveryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Delivery in the database
        List<Delivery> deliveryList = deliveryRepository.findAll();
        assertThat(deliveryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDeliveryAddrIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryRepository.findAll().size();
        // set the field null
        delivery.setDeliveryAddr(null);

        // Create the Delivery, which fails.
        DeliveryDTO deliveryDTO = deliveryMapper.toDto(delivery);

        restDeliveryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryDTO)))
            .andExpect(status().isBadRequest());

        List<Delivery> deliveryList = deliveryRepository.findAll();
        assertThat(deliveryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDistanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryRepository.findAll().size();
        // set the field null
        delivery.setDistance(null);

        // Create the Delivery, which fails.
        DeliveryDTO deliveryDTO = deliveryMapper.toDto(delivery);

        restDeliveryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryDTO)))
            .andExpect(status().isBadRequest());

        List<Delivery> deliveryList = deliveryRepository.findAll();
        assertThat(deliveryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryRepository.findAll().size();
        // set the field null
        delivery.setPrice(null);

        // Create the Delivery, which fails.
        DeliveryDTO deliveryDTO = deliveryMapper.toDto(delivery);

        restDeliveryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryDTO)))
            .andExpect(status().isBadRequest());

        List<Delivery> deliveryList = deliveryRepository.findAll();
        assertThat(deliveryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeliveries() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList
        restDeliveryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(delivery.getId().intValue())))
            .andExpect(jsonPath("$.[*].deliveryAddr").value(hasItem(DEFAULT_DELIVERY_ADDR)))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getDelivery() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get the delivery
        restDeliveryMockMvc
            .perform(get(ENTITY_API_URL_ID, delivery.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(delivery.getId().intValue()))
            .andExpect(jsonPath("$.deliveryAddr").value(DEFAULT_DELIVERY_ADDR))
            .andExpect(jsonPath("$.distance").value(DEFAULT_DISTANCE))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getDeliveriesByIdFiltering() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        Long id = delivery.getId();

        defaultDeliveryShouldBeFound("id.equals=" + id);
        defaultDeliveryShouldNotBeFound("id.notEquals=" + id);

        defaultDeliveryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDeliveryShouldNotBeFound("id.greaterThan=" + id);

        defaultDeliveryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDeliveryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDeliveriesByDeliveryAddrIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList where deliveryAddr equals to DEFAULT_DELIVERY_ADDR
        defaultDeliveryShouldBeFound("deliveryAddr.equals=" + DEFAULT_DELIVERY_ADDR);

        // Get all the deliveryList where deliveryAddr equals to UPDATED_DELIVERY_ADDR
        defaultDeliveryShouldNotBeFound("deliveryAddr.equals=" + UPDATED_DELIVERY_ADDR);
    }

    @Test
    @Transactional
    void getAllDeliveriesByDeliveryAddrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList where deliveryAddr not equals to DEFAULT_DELIVERY_ADDR
        defaultDeliveryShouldNotBeFound("deliveryAddr.notEquals=" + DEFAULT_DELIVERY_ADDR);

        // Get all the deliveryList where deliveryAddr not equals to UPDATED_DELIVERY_ADDR
        defaultDeliveryShouldBeFound("deliveryAddr.notEquals=" + UPDATED_DELIVERY_ADDR);
    }

    @Test
    @Transactional
    void getAllDeliveriesByDeliveryAddrIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList where deliveryAddr in DEFAULT_DELIVERY_ADDR or UPDATED_DELIVERY_ADDR
        defaultDeliveryShouldBeFound("deliveryAddr.in=" + DEFAULT_DELIVERY_ADDR + "," + UPDATED_DELIVERY_ADDR);

        // Get all the deliveryList where deliveryAddr equals to UPDATED_DELIVERY_ADDR
        defaultDeliveryShouldNotBeFound("deliveryAddr.in=" + UPDATED_DELIVERY_ADDR);
    }

    @Test
    @Transactional
    void getAllDeliveriesByDeliveryAddrIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList where deliveryAddr is not null
        defaultDeliveryShouldBeFound("deliveryAddr.specified=true");

        // Get all the deliveryList where deliveryAddr is null
        defaultDeliveryShouldNotBeFound("deliveryAddr.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveriesByDeliveryAddrContainsSomething() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList where deliveryAddr contains DEFAULT_DELIVERY_ADDR
        defaultDeliveryShouldBeFound("deliveryAddr.contains=" + DEFAULT_DELIVERY_ADDR);

        // Get all the deliveryList where deliveryAddr contains UPDATED_DELIVERY_ADDR
        defaultDeliveryShouldNotBeFound("deliveryAddr.contains=" + UPDATED_DELIVERY_ADDR);
    }

    @Test
    @Transactional
    void getAllDeliveriesByDeliveryAddrNotContainsSomething() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList where deliveryAddr does not contain DEFAULT_DELIVERY_ADDR
        defaultDeliveryShouldNotBeFound("deliveryAddr.doesNotContain=" + DEFAULT_DELIVERY_ADDR);

        // Get all the deliveryList where deliveryAddr does not contain UPDATED_DELIVERY_ADDR
        defaultDeliveryShouldBeFound("deliveryAddr.doesNotContain=" + UPDATED_DELIVERY_ADDR);
    }

    @Test
    @Transactional
    void getAllDeliveriesByDistanceIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList where distance equals to DEFAULT_DISTANCE
        defaultDeliveryShouldBeFound("distance.equals=" + DEFAULT_DISTANCE);

        // Get all the deliveryList where distance equals to UPDATED_DISTANCE
        defaultDeliveryShouldNotBeFound("distance.equals=" + UPDATED_DISTANCE);
    }

    @Test
    @Transactional
    void getAllDeliveriesByDistanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList where distance not equals to DEFAULT_DISTANCE
        defaultDeliveryShouldNotBeFound("distance.notEquals=" + DEFAULT_DISTANCE);

        // Get all the deliveryList where distance not equals to UPDATED_DISTANCE
        defaultDeliveryShouldBeFound("distance.notEquals=" + UPDATED_DISTANCE);
    }

    @Test
    @Transactional
    void getAllDeliveriesByDistanceIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList where distance in DEFAULT_DISTANCE or UPDATED_DISTANCE
        defaultDeliveryShouldBeFound("distance.in=" + DEFAULT_DISTANCE + "," + UPDATED_DISTANCE);

        // Get all the deliveryList where distance equals to UPDATED_DISTANCE
        defaultDeliveryShouldNotBeFound("distance.in=" + UPDATED_DISTANCE);
    }

    @Test
    @Transactional
    void getAllDeliveriesByDistanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList where distance is not null
        defaultDeliveryShouldBeFound("distance.specified=true");

        // Get all the deliveryList where distance is null
        defaultDeliveryShouldNotBeFound("distance.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveriesByDistanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList where distance is greater than or equal to DEFAULT_DISTANCE
        defaultDeliveryShouldBeFound("distance.greaterThanOrEqual=" + DEFAULT_DISTANCE);

        // Get all the deliveryList where distance is greater than or equal to UPDATED_DISTANCE
        defaultDeliveryShouldNotBeFound("distance.greaterThanOrEqual=" + UPDATED_DISTANCE);
    }

    @Test
    @Transactional
    void getAllDeliveriesByDistanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList where distance is less than or equal to DEFAULT_DISTANCE
        defaultDeliveryShouldBeFound("distance.lessThanOrEqual=" + DEFAULT_DISTANCE);

        // Get all the deliveryList where distance is less than or equal to SMALLER_DISTANCE
        defaultDeliveryShouldNotBeFound("distance.lessThanOrEqual=" + SMALLER_DISTANCE);
    }

    @Test
    @Transactional
    void getAllDeliveriesByDistanceIsLessThanSomething() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList where distance is less than DEFAULT_DISTANCE
        defaultDeliveryShouldNotBeFound("distance.lessThan=" + DEFAULT_DISTANCE);

        // Get all the deliveryList where distance is less than UPDATED_DISTANCE
        defaultDeliveryShouldBeFound("distance.lessThan=" + UPDATED_DISTANCE);
    }

    @Test
    @Transactional
    void getAllDeliveriesByDistanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList where distance is greater than DEFAULT_DISTANCE
        defaultDeliveryShouldNotBeFound("distance.greaterThan=" + DEFAULT_DISTANCE);

        // Get all the deliveryList where distance is greater than SMALLER_DISTANCE
        defaultDeliveryShouldBeFound("distance.greaterThan=" + SMALLER_DISTANCE);
    }

    @Test
    @Transactional
    void getAllDeliveriesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList where price equals to DEFAULT_PRICE
        defaultDeliveryShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the deliveryList where price equals to UPDATED_PRICE
        defaultDeliveryShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllDeliveriesByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList where price not equals to DEFAULT_PRICE
        defaultDeliveryShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the deliveryList where price not equals to UPDATED_PRICE
        defaultDeliveryShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllDeliveriesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultDeliveryShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the deliveryList where price equals to UPDATED_PRICE
        defaultDeliveryShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllDeliveriesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList where price is not null
        defaultDeliveryShouldBeFound("price.specified=true");

        // Get all the deliveryList where price is null
        defaultDeliveryShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllDeliveriesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList where price is greater than or equal to DEFAULT_PRICE
        defaultDeliveryShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the deliveryList where price is greater than or equal to UPDATED_PRICE
        defaultDeliveryShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllDeliveriesByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList where price is less than or equal to DEFAULT_PRICE
        defaultDeliveryShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the deliveryList where price is less than or equal to SMALLER_PRICE
        defaultDeliveryShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllDeliveriesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList where price is less than DEFAULT_PRICE
        defaultDeliveryShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the deliveryList where price is less than UPDATED_PRICE
        defaultDeliveryShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllDeliveriesByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        // Get all the deliveryList where price is greater than DEFAULT_PRICE
        defaultDeliveryShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the deliveryList where price is greater than SMALLER_PRICE
        defaultDeliveryShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllDeliveriesByDelivererIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);
        Deliverer deliverer = DelivererResourceIT.createEntity(em);
        em.persist(deliverer);
        em.flush();
        delivery.setDeliverer(deliverer);
        deliveryRepository.saveAndFlush(delivery);
        Long delivererId = deliverer.getId();

        // Get all the deliveryList where deliverer equals to delivererId
        defaultDeliveryShouldBeFound("delivererId.equals=" + delivererId);

        // Get all the deliveryList where deliverer equals to (delivererId + 1)
        defaultDeliveryShouldNotBeFound("delivererId.equals=" + (delivererId + 1));
    }

    @Test
    @Transactional
    void getAllDeliveriesByCommandIsEqualToSomething() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);
        Command command = CommandResourceIT.createEntity(em);
        em.persist(command);
        em.flush();
        delivery.addCommand(command);
        deliveryRepository.saveAndFlush(delivery);
        Long commandId = command.getId();

        // Get all the deliveryList where command equals to commandId
        defaultDeliveryShouldBeFound("commandId.equals=" + commandId);

        // Get all the deliveryList where command equals to (commandId + 1)
        defaultDeliveryShouldNotBeFound("commandId.equals=" + (commandId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDeliveryShouldBeFound(String filter) throws Exception {
        restDeliveryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(delivery.getId().intValue())))
            .andExpect(jsonPath("$.[*].deliveryAddr").value(hasItem(DEFAULT_DELIVERY_ADDR)))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));

        // Check, that the count call also returns 1
        restDeliveryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDeliveryShouldNotBeFound(String filter) throws Exception {
        restDeliveryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDeliveryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDelivery() throws Exception {
        // Get the delivery
        restDeliveryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDelivery() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        int databaseSizeBeforeUpdate = deliveryRepository.findAll().size();

        // Update the delivery
        Delivery updatedDelivery = deliveryRepository.findById(delivery.getId()).get();
        // Disconnect from session so that the updates on updatedDelivery are not directly saved in db
        em.detach(updatedDelivery);
        updatedDelivery.deliveryAddr(UPDATED_DELIVERY_ADDR).distance(UPDATED_DISTANCE).price(UPDATED_PRICE);
        DeliveryDTO deliveryDTO = deliveryMapper.toDto(updatedDelivery);

        restDeliveryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deliveryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Delivery in the database
        List<Delivery> deliveryList = deliveryRepository.findAll();
        assertThat(deliveryList).hasSize(databaseSizeBeforeUpdate);
        Delivery testDelivery = deliveryList.get(deliveryList.size() - 1);
        assertThat(testDelivery.getDeliveryAddr()).isEqualTo(UPDATED_DELIVERY_ADDR);
        assertThat(testDelivery.getDistance()).isEqualTo(UPDATED_DISTANCE);
        assertThat(testDelivery.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingDelivery() throws Exception {
        int databaseSizeBeforeUpdate = deliveryRepository.findAll().size();
        delivery.setId(count.incrementAndGet());

        // Create the Delivery
        DeliveryDTO deliveryDTO = deliveryMapper.toDto(delivery);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deliveryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Delivery in the database
        List<Delivery> deliveryList = deliveryRepository.findAll();
        assertThat(deliveryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDelivery() throws Exception {
        int databaseSizeBeforeUpdate = deliveryRepository.findAll().size();
        delivery.setId(count.incrementAndGet());

        // Create the Delivery
        DeliveryDTO deliveryDTO = deliveryMapper.toDto(delivery);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliveryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Delivery in the database
        List<Delivery> deliveryList = deliveryRepository.findAll();
        assertThat(deliveryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDelivery() throws Exception {
        int databaseSizeBeforeUpdate = deliveryRepository.findAll().size();
        delivery.setId(count.incrementAndGet());

        // Create the Delivery
        DeliveryDTO deliveryDTO = deliveryMapper.toDto(delivery);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deliveryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Delivery in the database
        List<Delivery> deliveryList = deliveryRepository.findAll();
        assertThat(deliveryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeliveryWithPatch() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        int databaseSizeBeforeUpdate = deliveryRepository.findAll().size();

        // Update the delivery using partial update
        Delivery partialUpdatedDelivery = new Delivery();
        partialUpdatedDelivery.setId(delivery.getId());

        partialUpdatedDelivery.deliveryAddr(UPDATED_DELIVERY_ADDR);

        restDeliveryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDelivery.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDelivery))
            )
            .andExpect(status().isOk());

        // Validate the Delivery in the database
        List<Delivery> deliveryList = deliveryRepository.findAll();
        assertThat(deliveryList).hasSize(databaseSizeBeforeUpdate);
        Delivery testDelivery = deliveryList.get(deliveryList.size() - 1);
        assertThat(testDelivery.getDeliveryAddr()).isEqualTo(UPDATED_DELIVERY_ADDR);
        assertThat(testDelivery.getDistance()).isEqualTo(DEFAULT_DISTANCE);
        assertThat(testDelivery.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateDeliveryWithPatch() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        int databaseSizeBeforeUpdate = deliveryRepository.findAll().size();

        // Update the delivery using partial update
        Delivery partialUpdatedDelivery = new Delivery();
        partialUpdatedDelivery.setId(delivery.getId());

        partialUpdatedDelivery.deliveryAddr(UPDATED_DELIVERY_ADDR).distance(UPDATED_DISTANCE).price(UPDATED_PRICE);

        restDeliveryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDelivery.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDelivery))
            )
            .andExpect(status().isOk());

        // Validate the Delivery in the database
        List<Delivery> deliveryList = deliveryRepository.findAll();
        assertThat(deliveryList).hasSize(databaseSizeBeforeUpdate);
        Delivery testDelivery = deliveryList.get(deliveryList.size() - 1);
        assertThat(testDelivery.getDeliveryAddr()).isEqualTo(UPDATED_DELIVERY_ADDR);
        assertThat(testDelivery.getDistance()).isEqualTo(UPDATED_DISTANCE);
        assertThat(testDelivery.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingDelivery() throws Exception {
        int databaseSizeBeforeUpdate = deliveryRepository.findAll().size();
        delivery.setId(count.incrementAndGet());

        // Create the Delivery
        DeliveryDTO deliveryDTO = deliveryMapper.toDto(delivery);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deliveryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Delivery in the database
        List<Delivery> deliveryList = deliveryRepository.findAll();
        assertThat(deliveryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDelivery() throws Exception {
        int databaseSizeBeforeUpdate = deliveryRepository.findAll().size();
        delivery.setId(count.incrementAndGet());

        // Create the Delivery
        DeliveryDTO deliveryDTO = deliveryMapper.toDto(delivery);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliveryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Delivery in the database
        List<Delivery> deliveryList = deliveryRepository.findAll();
        assertThat(deliveryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDelivery() throws Exception {
        int databaseSizeBeforeUpdate = deliveryRepository.findAll().size();
        delivery.setId(count.incrementAndGet());

        // Create the Delivery
        DeliveryDTO deliveryDTO = deliveryMapper.toDto(delivery);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliveryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(deliveryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Delivery in the database
        List<Delivery> deliveryList = deliveryRepository.findAll();
        assertThat(deliveryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDelivery() throws Exception {
        // Initialize the database
        deliveryRepository.saveAndFlush(delivery);

        int databaseSizeBeforeDelete = deliveryRepository.findAll().size();

        // Delete the delivery
        restDeliveryMockMvc
            .perform(delete(ENTITY_API_URL_ID, delivery.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Delivery> deliveryList = deliveryRepository.findAll();
        assertThat(deliveryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
