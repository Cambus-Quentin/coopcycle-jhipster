package fr.polytech.info4.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Restaurant.
 */
@Entity
@Table(name = "restaurant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Restaurant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Size(min = 3)
    @Column(name = "address", nullable = false)
    private String address;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_restaurant__local_cooperative",
        joinColumns = @JoinColumn(name = "restaurant_id"),
        inverseJoinColumns = @JoinColumn(name = "local_cooperative_id")
    )
    @JsonIgnoreProperties(value = { "nationalCooperative", "restaurants" }, allowSetters = true)
    private Set<LocalCooperative> localCooperatives = new HashSet<>();

    @OneToMany(mappedBy = "restaurant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "restaurant", "commands" }, allowSetters = true)
    private Set<Dish> dishes = new HashSet<>();

    @OneToMany(mappedBy = "restaurant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "client", "delivery", "restaurant", "dishes" }, allowSetters = true)
    private Set<Command> commands = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Restaurant id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Restaurant name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public Restaurant address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return this.user;
    }

    public Restaurant user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<LocalCooperative> getLocalCooperatives() {
        return this.localCooperatives;
    }

    public Restaurant localCooperatives(Set<LocalCooperative> localCooperatives) {
        this.setLocalCooperatives(localCooperatives);
        return this;
    }

    public Restaurant addLocalCooperative(LocalCooperative localCooperative) {
        this.localCooperatives.add(localCooperative);
        localCooperative.getRestaurants().add(this);
        return this;
    }

    public Restaurant removeLocalCooperative(LocalCooperative localCooperative) {
        this.localCooperatives.remove(localCooperative);
        localCooperative.getRestaurants().remove(this);
        return this;
    }

    public void setLocalCooperatives(Set<LocalCooperative> localCooperatives) {
        this.localCooperatives = localCooperatives;
    }

    public Set<Dish> getDishes() {
        return this.dishes;
    }

    public Restaurant dishes(Set<Dish> dishes) {
        this.setDishes(dishes);
        return this;
    }

    public Restaurant addDish(Dish dish) {
        this.dishes.add(dish);
        dish.setRestaurant(this);
        return this;
    }

    public Restaurant removeDish(Dish dish) {
        this.dishes.remove(dish);
        dish.setRestaurant(null);
        return this;
    }

    public void setDishes(Set<Dish> dishes) {
        if (this.dishes != null) {
            this.dishes.forEach(i -> i.setRestaurant(null));
        }
        if (dishes != null) {
            dishes.forEach(i -> i.setRestaurant(this));
        }
        this.dishes = dishes;
    }

    public Set<Command> getCommands() {
        return this.commands;
    }

    public Restaurant commands(Set<Command> commands) {
        this.setCommands(commands);
        return this;
    }

    public Restaurant addCommand(Command command) {
        this.commands.add(command);
        command.setRestaurant(this);
        return this;
    }

    public Restaurant removeCommand(Command command) {
        this.commands.remove(command);
        command.setRestaurant(null);
        return this;
    }

    public void setCommands(Set<Command> commands) {
        if (this.commands != null) {
            this.commands.forEach(i -> i.setRestaurant(null));
        }
        if (commands != null) {
            commands.forEach(i -> i.setRestaurant(this));
        }
        this.commands = commands;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Restaurant)) {
            return false;
        }
        return id != null && id.equals(((Restaurant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Restaurant{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
