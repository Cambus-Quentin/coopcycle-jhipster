package fr.polytech.info4.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.polytech.info4.IntegrationTest;
import fr.polytech.info4.domain.Client;
import fr.polytech.info4.domain.Command;
import fr.polytech.info4.domain.Delivery;
import fr.polytech.info4.domain.Dish;
import fr.polytech.info4.domain.Restaurant;
import fr.polytech.info4.domain.enumeration.STATECMD;
import fr.polytech.info4.repository.CommandRepository;
import fr.polytech.info4.service.CommandService;
import fr.polytech.info4.service.criteria.CommandCriteria;
import fr.polytech.info4.service.dto.CommandDTO;
import fr.polytech.info4.service.mapper.CommandMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CommandResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CommandResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;
    private static final Float SMALLER_PRICE = 1F - 1F;

    private static final STATECMD DEFAULT_STATE = STATECMD.PREPARATION;
    private static final STATECMD UPDATED_STATE = STATECMD.SUPPORTED;

    private static final String ENTITY_API_URL = "/api/commands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommandRepository commandRepository;

    @Mock
    private CommandRepository commandRepositoryMock;

    @Autowired
    private CommandMapper commandMapper;

    @Mock
    private CommandService commandServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommandMockMvc;

    private Command command;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Command createEntity(EntityManager em) {
        Command command = new Command().date(DEFAULT_DATE).price(DEFAULT_PRICE).state(DEFAULT_STATE);
        return command;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Command createUpdatedEntity(EntityManager em) {
        Command command = new Command().date(UPDATED_DATE).price(UPDATED_PRICE).state(UPDATED_STATE);
        return command;
    }

    @BeforeEach
    public void initTest() {
        command = createEntity(em);
    }

    @Test
    @Transactional
    void createCommand() throws Exception {
        int databaseSizeBeforeCreate = commandRepository.findAll().size();
        // Create the Command
        CommandDTO commandDTO = commandMapper.toDto(command);
        restCommandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandDTO)))
            .andExpect(status().isCreated());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeCreate + 1);
        Command testCommand = commandList.get(commandList.size() - 1);
        assertThat(testCommand.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testCommand.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCommand.getState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    void createCommandWithExistingId() throws Exception {
        // Create the Command with an existing ID
        command.setId(1L);
        CommandDTO commandDTO = commandMapper.toDto(command);

        int databaseSizeBeforeCreate = commandRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandRepository.findAll().size();
        // set the field null
        command.setDate(null);

        // Create the Command, which fails.
        CommandDTO commandDTO = commandMapper.toDto(command);

        restCommandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandDTO)))
            .andExpect(status().isBadRequest());

        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandRepository.findAll().size();
        // set the field null
        command.setPrice(null);

        // Create the Command, which fails.
        CommandDTO commandDTO = commandMapper.toDto(command);

        restCommandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandDTO)))
            .andExpect(status().isBadRequest());

        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCommands() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList
        restCommandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(command.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCommandsWithEagerRelationshipsIsEnabled() throws Exception {
        when(commandServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCommandMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(commandServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCommandsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(commandServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCommandMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(commandServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCommand() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get the command
        restCommandMockMvc
            .perform(get(ENTITY_API_URL_ID, command.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(command.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    void getCommandsByIdFiltering() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        Long id = command.getId();

        defaultCommandShouldBeFound("id.equals=" + id);
        defaultCommandShouldNotBeFound("id.notEquals=" + id);

        defaultCommandShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommandShouldNotBeFound("id.greaterThan=" + id);

        defaultCommandShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommandShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommandsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where date equals to DEFAULT_DATE
        defaultCommandShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the commandList where date equals to UPDATED_DATE
        defaultCommandShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCommandsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where date not equals to DEFAULT_DATE
        defaultCommandShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the commandList where date not equals to UPDATED_DATE
        defaultCommandShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCommandsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where date in DEFAULT_DATE or UPDATED_DATE
        defaultCommandShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the commandList where date equals to UPDATED_DATE
        defaultCommandShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCommandsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where date is not null
        defaultCommandShouldBeFound("date.specified=true");

        // Get all the commandList where date is null
        defaultCommandShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllCommandsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where date is greater than or equal to DEFAULT_DATE
        defaultCommandShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the commandList where date is greater than or equal to UPDATED_DATE
        defaultCommandShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCommandsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where date is less than or equal to DEFAULT_DATE
        defaultCommandShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the commandList where date is less than or equal to SMALLER_DATE
        defaultCommandShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllCommandsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where date is less than DEFAULT_DATE
        defaultCommandShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the commandList where date is less than UPDATED_DATE
        defaultCommandShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCommandsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where date is greater than DEFAULT_DATE
        defaultCommandShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the commandList where date is greater than SMALLER_DATE
        defaultCommandShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllCommandsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where price equals to DEFAULT_PRICE
        defaultCommandShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the commandList where price equals to UPDATED_PRICE
        defaultCommandShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCommandsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where price not equals to DEFAULT_PRICE
        defaultCommandShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the commandList where price not equals to UPDATED_PRICE
        defaultCommandShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCommandsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultCommandShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the commandList where price equals to UPDATED_PRICE
        defaultCommandShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCommandsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where price is not null
        defaultCommandShouldBeFound("price.specified=true");

        // Get all the commandList where price is null
        defaultCommandShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllCommandsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where price is greater than or equal to DEFAULT_PRICE
        defaultCommandShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the commandList where price is greater than or equal to UPDATED_PRICE
        defaultCommandShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCommandsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where price is less than or equal to DEFAULT_PRICE
        defaultCommandShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the commandList where price is less than or equal to SMALLER_PRICE
        defaultCommandShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllCommandsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where price is less than DEFAULT_PRICE
        defaultCommandShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the commandList where price is less than UPDATED_PRICE
        defaultCommandShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCommandsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where price is greater than DEFAULT_PRICE
        defaultCommandShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the commandList where price is greater than SMALLER_PRICE
        defaultCommandShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllCommandsByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where state equals to DEFAULT_STATE
        defaultCommandShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the commandList where state equals to UPDATED_STATE
        defaultCommandShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllCommandsByStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where state not equals to DEFAULT_STATE
        defaultCommandShouldNotBeFound("state.notEquals=" + DEFAULT_STATE);

        // Get all the commandList where state not equals to UPDATED_STATE
        defaultCommandShouldBeFound("state.notEquals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllCommandsByStateIsInShouldWork() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where state in DEFAULT_STATE or UPDATED_STATE
        defaultCommandShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the commandList where state equals to UPDATED_STATE
        defaultCommandShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllCommandsByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commandList where state is not null
        defaultCommandShouldBeFound("state.specified=true");

        // Get all the commandList where state is null
        defaultCommandShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    void getAllCommandsByClientIsEqualToSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);
        Client client = ClientResourceIT.createEntity(em);
        em.persist(client);
        em.flush();
        command.setClient(client);
        commandRepository.saveAndFlush(command);
        Long clientId = client.getId();

        // Get all the commandList where client equals to clientId
        defaultCommandShouldBeFound("clientId.equals=" + clientId);

        // Get all the commandList where client equals to (clientId + 1)
        defaultCommandShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    @Test
    @Transactional
    void getAllCommandsByDeliveryIsEqualToSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);
        Delivery delivery = DeliveryResourceIT.createEntity(em);
        em.persist(delivery);
        em.flush();
        command.setDelivery(delivery);
        commandRepository.saveAndFlush(command);
        Long deliveryId = delivery.getId();

        // Get all the commandList where delivery equals to deliveryId
        defaultCommandShouldBeFound("deliveryId.equals=" + deliveryId);

        // Get all the commandList where delivery equals to (deliveryId + 1)
        defaultCommandShouldNotBeFound("deliveryId.equals=" + (deliveryId + 1));
    }

    @Test
    @Transactional
    void getAllCommandsByRestaurantIsEqualToSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);
        Restaurant restaurant = RestaurantResourceIT.createEntity(em);
        em.persist(restaurant);
        em.flush();
        command.setRestaurant(restaurant);
        commandRepository.saveAndFlush(command);
        Long restaurantId = restaurant.getId();

        // Get all the commandList where restaurant equals to restaurantId
        defaultCommandShouldBeFound("restaurantId.equals=" + restaurantId);

        // Get all the commandList where restaurant equals to (restaurantId + 1)
        defaultCommandShouldNotBeFound("restaurantId.equals=" + (restaurantId + 1));
    }

    @Test
    @Transactional
    void getAllCommandsByDishIsEqualToSomething() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);
        Dish dish = DishResourceIT.createEntity(em);
        em.persist(dish);
        em.flush();
        command.addDish(dish);
        commandRepository.saveAndFlush(command);
        Long dishId = dish.getId();

        // Get all the commandList where dish equals to dishId
        defaultCommandShouldBeFound("dishId.equals=" + dishId);

        // Get all the commandList where dish equals to (dishId + 1)
        defaultCommandShouldNotBeFound("dishId.equals=" + (dishId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommandShouldBeFound(String filter) throws Exception {
        restCommandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(command.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));

        // Check, that the count call also returns 1
        restCommandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommandShouldNotBeFound(String filter) throws Exception {
        restCommandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommand() throws Exception {
        // Get the command
        restCommandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommand() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        int databaseSizeBeforeUpdate = commandRepository.findAll().size();

        // Update the command
        Command updatedCommand = commandRepository.findById(command.getId()).get();
        // Disconnect from session so that the updates on updatedCommand are not directly saved in db
        em.detach(updatedCommand);
        updatedCommand.date(UPDATED_DATE).price(UPDATED_PRICE).state(UPDATED_STATE);
        CommandDTO commandDTO = commandMapper.toDto(updatedCommand);

        restCommandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commandDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandDTO))
            )
            .andExpect(status().isOk());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
        Command testCommand = commandList.get(commandList.size() - 1);
        assertThat(testCommand.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCommand.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCommand.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    void putNonExistingCommand() throws Exception {
        int databaseSizeBeforeUpdate = commandRepository.findAll().size();
        command.setId(count.incrementAndGet());

        // Create the Command
        CommandDTO commandDTO = commandMapper.toDto(command);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commandDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommand() throws Exception {
        int databaseSizeBeforeUpdate = commandRepository.findAll().size();
        command.setId(count.incrementAndGet());

        // Create the Command
        CommandDTO commandDTO = commandMapper.toDto(command);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommand() throws Exception {
        int databaseSizeBeforeUpdate = commandRepository.findAll().size();
        command.setId(count.incrementAndGet());

        // Create the Command
        CommandDTO commandDTO = commandMapper.toDto(command);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommandWithPatch() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        int databaseSizeBeforeUpdate = commandRepository.findAll().size();

        // Update the command using partial update
        Command partialUpdatedCommand = new Command();
        partialUpdatedCommand.setId(command.getId());

        partialUpdatedCommand.date(UPDATED_DATE);

        restCommandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommand.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommand))
            )
            .andExpect(status().isOk());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
        Command testCommand = commandList.get(commandList.size() - 1);
        assertThat(testCommand.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCommand.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCommand.getState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    void fullUpdateCommandWithPatch() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        int databaseSizeBeforeUpdate = commandRepository.findAll().size();

        // Update the command using partial update
        Command partialUpdatedCommand = new Command();
        partialUpdatedCommand.setId(command.getId());

        partialUpdatedCommand.date(UPDATED_DATE).price(UPDATED_PRICE).state(UPDATED_STATE);

        restCommandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommand.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommand))
            )
            .andExpect(status().isOk());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
        Command testCommand = commandList.get(commandList.size() - 1);
        assertThat(testCommand.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCommand.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCommand.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    void patchNonExistingCommand() throws Exception {
        int databaseSizeBeforeUpdate = commandRepository.findAll().size();
        command.setId(count.incrementAndGet());

        // Create the Command
        CommandDTO commandDTO = commandMapper.toDto(command);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commandDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommand() throws Exception {
        int databaseSizeBeforeUpdate = commandRepository.findAll().size();
        command.setId(count.incrementAndGet());

        // Create the Command
        CommandDTO commandDTO = commandMapper.toDto(command);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommand() throws Exception {
        int databaseSizeBeforeUpdate = commandRepository.findAll().size();
        command.setId(count.incrementAndGet());

        // Create the Command
        CommandDTO commandDTO = commandMapper.toDto(command);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(commandDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Command in the database
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommand() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        int databaseSizeBeforeDelete = commandRepository.findAll().size();

        // Delete the command
        restCommandMockMvc
            .perform(delete(ENTITY_API_URL_ID, command.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Command> commandList = commandRepository.findAll();
        assertThat(commandList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
