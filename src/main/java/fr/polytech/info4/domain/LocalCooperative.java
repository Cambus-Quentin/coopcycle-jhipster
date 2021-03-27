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
 * A LocalCooperative.
 */
@Entity
@Table(name = "local_cooperative")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LocalCooperative implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "geo_zone", nullable = false)
    private String geoZone;

    @ManyToOne
    @JsonIgnoreProperties(value = { "localCooperatives" }, allowSetters = true)
    private NationalCooperative nationalCooperative;

    @ManyToMany(mappedBy = "localCooperatives")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "localCooperatives", "dishes", "commands" }, allowSetters = true)
    private Set<Restaurant> restaurants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalCooperative id(Long id) {
        this.id = id;
        return this;
    }

    public String getGeoZone() {
        return this.geoZone;
    }

    public LocalCooperative geoZone(String geoZone) {
        this.geoZone = geoZone;
        return this;
    }

    public void setGeoZone(String geoZone) {
        this.geoZone = geoZone;
    }

    public NationalCooperative getNationalCooperative() {
        return this.nationalCooperative;
    }

    public LocalCooperative nationalCooperative(NationalCooperative nationalCooperative) {
        this.setNationalCooperative(nationalCooperative);
        return this;
    }

    public void setNationalCooperative(NationalCooperative nationalCooperative) {
        this.nationalCooperative = nationalCooperative;
    }

    public Set<Restaurant> getRestaurants() {
        return this.restaurants;
    }

    public LocalCooperative restaurants(Set<Restaurant> restaurants) {
        this.setRestaurants(restaurants);
        return this;
    }

    public LocalCooperative addRestaurant(Restaurant restaurant) {
        this.restaurants.add(restaurant);
        restaurant.getLocalCooperatives().add(this);
        return this;
    }

    public LocalCooperative removeRestaurant(Restaurant restaurant) {
        this.restaurants.remove(restaurant);
        restaurant.getLocalCooperatives().remove(this);
        return this;
    }

    public void setRestaurants(Set<Restaurant> restaurants) {
        if (this.restaurants != null) {
            this.restaurants.forEach(i -> i.removeLocalCooperative(this));
        }
        if (restaurants != null) {
            restaurants.forEach(i -> i.addLocalCooperative(this));
        }
        this.restaurants = restaurants;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocalCooperative)) {
            return false;
        }
        return id != null && id.equals(((LocalCooperative) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocalCooperative{" +
            "id=" + getId() +
            ", geoZone='" + getGeoZone() + "'" +
            "}";
    }
}
