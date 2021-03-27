package fr.polytech.info4.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fr.polytech.info4.domain.Delivery} entity.
 */
public class DeliveryDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3)
    private String deliveryAddr;

    @NotNull
    private Integer distance;

    @NotNull
    private Float price;

    private DelivererDTO deliverer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeliveryAddr() {
        return deliveryAddr;
    }

    public void setDeliveryAddr(String deliveryAddr) {
        this.deliveryAddr = deliveryAddr;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public DelivererDTO getDeliverer() {
        return deliverer;
    }

    public void setDeliverer(DelivererDTO deliverer) {
        this.deliverer = deliverer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeliveryDTO)) {
            return false;
        }

        DeliveryDTO deliveryDTO = (DeliveryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deliveryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryDTO{" +
            "id=" + getId() +
            ", deliveryAddr='" + getDeliveryAddr() + "'" +
            ", distance=" + getDistance() +
            ", price=" + getPrice() +
            ", deliverer=" + getDeliverer() +
            "}";
    }
}
