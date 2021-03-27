package fr.polytech.info4.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fr.polytech.info4.domain.Deliverer} entity.
 */
public class DelivererDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String firstname;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DelivererDTO)) {
            return false;
        }

        DelivererDTO delivererDTO = (DelivererDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, delivererDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DelivererDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
