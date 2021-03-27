package fr.polytech.info4.service.dto;

import fr.polytech.info4.domain.enumeration.STATECMD;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fr.polytech.info4.domain.Command} entity.
 */
public class CommandDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private Float price;

    private STATECMD state;

    private ClientDTO client;

    private DeliveryDTO delivery;

    private RestaurantDTO restaurant;

    private Set<DishDTO> dishes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public STATECMD getState() {
        return state;
    }

    public void setState(STATECMD state) {
        this.state = state;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public DeliveryDTO getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryDTO delivery) {
        this.delivery = delivery;
    }

    public RestaurantDTO getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantDTO restaurant) {
        this.restaurant = restaurant;
    }

    public Set<DishDTO> getDishes() {
        return dishes;
    }

    public void setDishes(Set<DishDTO> dishes) {
        this.dishes = dishes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommandDTO)) {
            return false;
        }

        CommandDTO commandDTO = (CommandDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commandDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommandDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", price=" + getPrice() +
            ", state='" + getState() + "'" +
            ", client=" + getClient() +
            ", delivery=" + getDelivery() +
            ", restaurant=" + getRestaurant() +
            ", dishes=" + getDishes() +
            "}";
    }
}
