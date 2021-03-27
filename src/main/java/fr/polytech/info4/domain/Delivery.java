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
 * A Delivery.
 */
@Entity
@Table(name = "delivery")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Delivery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "delivery_addr", nullable = false)
    private String deliveryAddr;

    @NotNull
    @Column(name = "distance", nullable = false)
    private Integer distance;

    @NotNull
    @Column(name = "price", nullable = false)
    private Float price;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "deliveries" }, allowSetters = true)
    private Deliverer deliverer;

    @OneToMany(mappedBy = "delivery")
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

    public Delivery id(Long id) {
        this.id = id;
        return this;
    }

    public String getDeliveryAddr() {
        return this.deliveryAddr;
    }

    public Delivery deliveryAddr(String deliveryAddr) {
        this.deliveryAddr = deliveryAddr;
        return this;
    }

    public void setDeliveryAddr(String deliveryAddr) {
        this.deliveryAddr = deliveryAddr;
    }

    public Integer getDistance() {
        return this.distance;
    }

    public Delivery distance(Integer distance) {
        this.distance = distance;
        return this;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Float getPrice() {
        return this.price;
    }

    public Delivery price(Float price) {
        this.price = price;
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Deliverer getDeliverer() {
        return this.deliverer;
    }

    public Delivery deliverer(Deliverer deliverer) {
        this.setDeliverer(deliverer);
        return this;
    }

    public void setDeliverer(Deliverer deliverer) {
        this.deliverer = deliverer;
    }

    public Set<Command> getCommands() {
        return this.commands;
    }

    public Delivery commands(Set<Command> commands) {
        this.setCommands(commands);
        return this;
    }

    public Delivery addCommand(Command command) {
        this.commands.add(command);
        command.setDelivery(this);
        return this;
    }

    public Delivery removeCommand(Command command) {
        this.commands.remove(command);
        command.setDelivery(null);
        return this;
    }

    public void setCommands(Set<Command> commands) {
        if (this.commands != null) {
            this.commands.forEach(i -> i.setDelivery(null));
        }
        if (commands != null) {
            commands.forEach(i -> i.setDelivery(this));
        }
        this.commands = commands;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Delivery)) {
            return false;
        }
        return id != null && id.equals(((Delivery) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Delivery{" +
            "id=" + getId() +
            ", deliveryAddr='" + getDeliveryAddr() + "'" +
            ", distance=" + getDistance() +
            ", price=" + getPrice() +
            "}";
    }
}
