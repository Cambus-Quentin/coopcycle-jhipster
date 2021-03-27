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
 * A NationalCooperative.
 */
@Entity
@Table(name = "national_cooperative")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NationalCooperative implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "nationalCooperative")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "nationalCooperative", "restaurants" }, allowSetters = true)
    private Set<LocalCooperative> localCooperatives = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NationalCooperative id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public NationalCooperative name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<LocalCooperative> getLocalCooperatives() {
        return this.localCooperatives;
    }

    public NationalCooperative localCooperatives(Set<LocalCooperative> localCooperatives) {
        this.setLocalCooperatives(localCooperatives);
        return this;
    }

    public NationalCooperative addLocalCooperative(LocalCooperative localCooperative) {
        this.localCooperatives.add(localCooperative);
        localCooperative.setNationalCooperative(this);
        return this;
    }

    public NationalCooperative removeLocalCooperative(LocalCooperative localCooperative) {
        this.localCooperatives.remove(localCooperative);
        localCooperative.setNationalCooperative(null);
        return this;
    }

    public void setLocalCooperatives(Set<LocalCooperative> localCooperatives) {
        if (this.localCooperatives != null) {
            this.localCooperatives.forEach(i -> i.setNationalCooperative(null));
        }
        if (localCooperatives != null) {
            localCooperatives.forEach(i -> i.setNationalCooperative(this));
        }
        this.localCooperatives = localCooperatives;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NationalCooperative)) {
            return false;
        }
        return id != null && id.equals(((NationalCooperative) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NationalCooperative{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
