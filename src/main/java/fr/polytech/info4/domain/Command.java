package fr.polytech.info4.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.polytech.info4.domain.enumeration.STATECMD;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Command.
 */
@Entity
@Table(name = "command")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Command implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "price", nullable = false)
    private Float price;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private STATECMD state;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "commands" }, allowSetters = true)
    private Client client;

    @ManyToOne
    @JsonIgnoreProperties(value = { "deliverer", "commands" }, allowSetters = true)
    private Delivery delivery;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "localCooperatives", "dishes", "commands" }, allowSetters = true)
    private Restaurant restaurant;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_command__dish",
        joinColumns = @JoinColumn(name = "command_id"),
        inverseJoinColumns = @JoinColumn(name = "dish_id")
    )
    @JsonIgnoreProperties(value = { "restaurant", "commands" }, allowSetters = true)
    private Set<Dish> dishes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Command id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Command date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Float getPrice() {
        return this.price;
    }

    public Command price(Float price) {
        this.price = price;
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public STATECMD getState() {
        return this.state;
    }

    public Command state(STATECMD state) {
        this.state = state;
        return this;
    }

    public void setState(STATECMD state) {
        this.state = state;
    }

    public Client getClient() {
        return this.client;
    }

    public Command client(Client client) {
        this.setClient(client);
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Delivery getDelivery() {
        return this.delivery;
    }

    public Command delivery(Delivery delivery) {
        this.setDelivery(delivery);
        return this;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }

    public Command restaurant(Restaurant restaurant) {
        this.setRestaurant(restaurant);
        return this;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Set<Dish> getDishes() {
        return this.dishes;
    }

    public Command dishes(Set<Dish> dishes) {
        this.setDishes(dishes);
        return this;
    }

    public Command addDish(Dish dish) {
        this.dishes.add(dish);
        dish.getCommands().add(this);
        return this;
    }

    public Command removeDish(Dish dish) {
        this.dishes.remove(dish);
        dish.getCommands().remove(this);
        return this;
    }

    public void setDishes(Set<Dish> dishes) {
        this.dishes = dishes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Command)) {
            return false;
        }
        return id != null && id.equals(((Command) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Command{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", price=" + getPrice() +
            ", state='" + getState() + "'" +
            "}";
    }
}
