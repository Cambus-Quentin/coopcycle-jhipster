package fr.polytech.info4.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.polytech.info4.IntegrationTest;
import fr.polytech.info4.domain.Command;
import fr.polytech.info4.domain.Dish;
import fr.polytech.info4.domain.Restaurant;
import fr.polytech.info4.repository.DishRepository;
import fr.polytech.info4.service.criteria.DishCriteria;
import fr.polytech.info4.service.dto.DishDTO;
import fr.polytech.info4.service.mapper.DishMapper;
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
 * Integration tests for the {@link DishResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DishResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;
    private static final Float SMALLER_PRICE = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/dishes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDishMockMvc;

    private Dish dish;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dish createEntity(EntityManager em) {
        Dish dish = new Dish().name(DEFAULT_NAME).price(DEFAULT_PRICE);
        return dish;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dish createUpdatedEntity(EntityManager em) {
        Dish dish = new Dish().name(UPDATED_NAME).price(UPDATED_PRICE);
        return dish;
    }

    @BeforeEach
    public void initTest() {
        dish = createEntity(em);
    }

    @Test
    @Transactional
    void createDish() throws Exception {
        int databaseSizeBeforeCreate = dishRepository.findAll().size();
        // Create the Dish
        DishDTO dishDTO = dishMapper.toDto(dish);
        restDishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dishDTO)))
            .andExpect(status().isCreated());

        // Validate the Dish in the database
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeCreate + 1);
        Dish testDish = dishList.get(dishList.size() - 1);
        assertThat(testDish.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDish.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createDishWithExistingId() throws Exception {
        // Create the Dish with an existing ID
        dish.setId(1L);
        DishDTO dishDTO = dishMapper.toDto(dish);

        int databaseSizeBeforeCreate = dishRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dishDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dish in the database
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dishRepository.findAll().size();
        // set the field null
        dish.setName(null);

        // Create the Dish, which fails.
        DishDTO dishDTO = dishMapper.toDto(dish);

        restDishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dishDTO)))
            .andExpect(status().isBadRequest());

        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = dishRepository.findAll().size();
        // set the field null
        dish.setPrice(null);

        // Create the Dish, which fails.
        DishDTO dishDTO = dishMapper.toDto(dish);

        restDishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dishDTO)))
            .andExpect(status().isBadRequest());

        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDishes() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        // Get all the dishList
        restDishMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dish.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getDish() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        // Get the dish
        restDishMockMvc
            .perform(get(ENTITY_API_URL_ID, dish.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dish.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getDishesByIdFiltering() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        Long id = dish.getId();

        defaultDishShouldBeFound("id.equals=" + id);
        defaultDishShouldNotBeFound("id.notEquals=" + id);

        defaultDishShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDishShouldNotBeFound("id.greaterThan=" + id);

        defaultDishShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDishShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDishesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        // Get all the dishList where name equals to DEFAULT_NAME
        defaultDishShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the dishList where name equals to UPDATED_NAME
        defaultDishShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDishesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        // Get all the dishList where name not equals to DEFAULT_NAME
        defaultDishShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the dishList where name not equals to UPDATED_NAME
        defaultDishShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDishesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        // Get all the dishList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDishShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the dishList where name equals to UPDATED_NAME
        defaultDishShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDishesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        // Get all the dishList where name is not null
        defaultDishShouldBeFound("name.specified=true");

        // Get all the dishList where name is null
        defaultDishShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllDishesByNameContainsSomething() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        // Get all the dishList where name contains DEFAULT_NAME
        defaultDishShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the dishList where name contains UPDATED_NAME
        defaultDishShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDishesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        // Get all the dishList where name does not contain DEFAULT_NAME
        defaultDishShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the dishList where name does not contain UPDATED_NAME
        defaultDishShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDishesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        // Get all the dishList where price equals to DEFAULT_PRICE
        defaultDishShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the dishList where price equals to UPDATED_PRICE
        defaultDishShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllDishesByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        // Get all the dishList where price not equals to DEFAULT_PRICE
        defaultDishShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the dishList where price not equals to UPDATED_PRICE
        defaultDishShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllDishesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        // Get all the dishList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultDishShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the dishList where price equals to UPDATED_PRICE
        defaultDishShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllDishesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        // Get all the dishList where price is not null
        defaultDishShouldBeFound("price.specified=true");

        // Get all the dishList where price is null
        defaultDishShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllDishesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        // Get all the dishList where price is greater than or equal to DEFAULT_PRICE
        defaultDishShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the dishList where price is greater than or equal to UPDATED_PRICE
        defaultDishShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllDishesByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        // Get all the dishList where price is less than or equal to DEFAULT_PRICE
        defaultDishShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the dishList where price is less than or equal to SMALLER_PRICE
        defaultDishShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllDishesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        // Get all the dishList where price is less than DEFAULT_PRICE
        defaultDishShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the dishList where price is less than UPDATED_PRICE
        defaultDishShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllDishesByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        // Get all the dishList where price is greater than DEFAULT_PRICE
        defaultDishShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the dishList where price is greater than SMALLER_PRICE
        defaultDishShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllDishesByRestaurantIsEqualToSomething() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);
        Restaurant restaurant = RestaurantResourceIT.createEntity(em);
        em.persist(restaurant);
        em.flush();
        dish.setRestaurant(restaurant);
        dishRepository.saveAndFlush(dish);
        Long restaurantId = restaurant.getId();

        // Get all the dishList where restaurant equals to restaurantId
        defaultDishShouldBeFound("restaurantId.equals=" + restaurantId);

        // Get all the dishList where restaurant equals to (restaurantId + 1)
        defaultDishShouldNotBeFound("restaurantId.equals=" + (restaurantId + 1));
    }

    @Test
    @Transactional
    void getAllDishesByCommandIsEqualToSomething() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);
        Command command = CommandResourceIT.createEntity(em);
        em.persist(command);
        em.flush();
        dish.addCommand(command);
        dishRepository.saveAndFlush(dish);
        Long commandId = command.getId();

        // Get all the dishList where command equals to commandId
        defaultDishShouldBeFound("commandId.equals=" + commandId);

        // Get all the dishList where command equals to (commandId + 1)
        defaultDishShouldNotBeFound("commandId.equals=" + (commandId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDishShouldBeFound(String filter) throws Exception {
        restDishMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dish.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));

        // Check, that the count call also returns 1
        restDishMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDishShouldNotBeFound(String filter) throws Exception {
        restDishMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDishMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDish() throws Exception {
        // Get the dish
        restDishMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDish() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        int databaseSizeBeforeUpdate = dishRepository.findAll().size();

        // Update the dish
        Dish updatedDish = dishRepository.findById(dish.getId()).get();
        // Disconnect from session so that the updates on updatedDish are not directly saved in db
        em.detach(updatedDish);
        updatedDish.name(UPDATED_NAME).price(UPDATED_PRICE);
        DishDTO dishDTO = dishMapper.toDto(updatedDish);

        restDishMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dishDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dishDTO))
            )
            .andExpect(status().isOk());

        // Validate the Dish in the database
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeUpdate);
        Dish testDish = dishList.get(dishList.size() - 1);
        assertThat(testDish.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDish.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingDish() throws Exception {
        int databaseSizeBeforeUpdate = dishRepository.findAll().size();
        dish.setId(count.incrementAndGet());

        // Create the Dish
        DishDTO dishDTO = dishMapper.toDto(dish);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dishDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dishDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dish in the database
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDish() throws Exception {
        int databaseSizeBeforeUpdate = dishRepository.findAll().size();
        dish.setId(count.incrementAndGet());

        // Create the Dish
        DishDTO dishDTO = dishMapper.toDto(dish);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dishDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dish in the database
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDish() throws Exception {
        int databaseSizeBeforeUpdate = dishRepository.findAll().size();
        dish.setId(count.incrementAndGet());

        // Create the Dish
        DishDTO dishDTO = dishMapper.toDto(dish);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dishDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dish in the database
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDishWithPatch() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        int databaseSizeBeforeUpdate = dishRepository.findAll().size();

        // Update the dish using partial update
        Dish partialUpdatedDish = new Dish();
        partialUpdatedDish.setId(dish.getId());

        partialUpdatedDish.price(UPDATED_PRICE);

        restDishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDish.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDish))
            )
            .andExpect(status().isOk());

        // Validate the Dish in the database
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeUpdate);
        Dish testDish = dishList.get(dishList.size() - 1);
        assertThat(testDish.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDish.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateDishWithPatch() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        int databaseSizeBeforeUpdate = dishRepository.findAll().size();

        // Update the dish using partial update
        Dish partialUpdatedDish = new Dish();
        partialUpdatedDish.setId(dish.getId());

        partialUpdatedDish.name(UPDATED_NAME).price(UPDATED_PRICE);

        restDishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDish.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDish))
            )
            .andExpect(status().isOk());

        // Validate the Dish in the database
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeUpdate);
        Dish testDish = dishList.get(dishList.size() - 1);
        assertThat(testDish.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDish.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingDish() throws Exception {
        int databaseSizeBeforeUpdate = dishRepository.findAll().size();
        dish.setId(count.incrementAndGet());

        // Create the Dish
        DishDTO dishDTO = dishMapper.toDto(dish);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dishDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dishDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dish in the database
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDish() throws Exception {
        int databaseSizeBeforeUpdate = dishRepository.findAll().size();
        dish.setId(count.incrementAndGet());

        // Create the Dish
        DishDTO dishDTO = dishMapper.toDto(dish);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dishDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dish in the database
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDish() throws Exception {
        int databaseSizeBeforeUpdate = dishRepository.findAll().size();
        dish.setId(count.incrementAndGet());

        // Create the Dish
        DishDTO dishDTO = dishMapper.toDto(dish);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDishMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dishDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dish in the database
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDish() throws Exception {
        // Initialize the database
        dishRepository.saveAndFlush(dish);

        int databaseSizeBeforeDelete = dishRepository.findAll().size();

        // Delete the dish
        restDishMockMvc
            .perform(delete(ENTITY_API_URL_ID, dish.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dish> dishList = dishRepository.findAll();
        assertThat(dishList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
