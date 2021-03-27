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
 * A Deliverer.
 */
@Entity
@Table(name = "deliverer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Deliverer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "firstname", nullable = false)
    private String firstname;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "deliverer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "deliverer", "commands" }, allowSetters = true)
    private Set<Delivery> deliveries = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Deliverer id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Deliverer name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public Deliverer firstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public User getUser() {
        return this.user;
    }

    public Deliverer user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Delivery> getDeliveries() {
        return this.deliveries;
    }

    public Deliverer deliveries(Set<Delivery> deliveries) {
        this.setDeliveries(deliveries);
        return this;
    }

    public Deliverer addDelivery(Delivery delivery) {
        this.deliveries.add(delivery);
        delivery.setDeliverer(this);
        return this;
    }

    public Deliverer removeDelivery(Delivery delivery) {
        this.deliveries.remove(delivery);
        delivery.setDeliverer(null);
        return this;
    }

    public void setDeliveries(Set<Delivery> deliveries) {
        if (this.deliveries != null) {
            this.deliveries.forEach(i -> i.setDeliverer(null));
        }
        if (deliveries != null) {
            deliveries.forEach(i -> i.setDeliverer(this));
        }
        this.deliveries = deliveries;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deliverer)) {
            return false;
        }
        return id != null && id.equals(((Deliverer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Deliverer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", firstname='" + getFirstname() + "'" +
            "}";
    }
}
