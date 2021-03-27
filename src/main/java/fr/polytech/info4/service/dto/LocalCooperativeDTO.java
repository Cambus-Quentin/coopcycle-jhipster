package fr.polytech.info4.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fr.polytech.info4.domain.LocalCooperative} entity.
 */
public class LocalCooperativeDTO implements Serializable {

    private Long id;

    @NotNull
    private String geoZone;

    private NationalCooperativeDTO nationalCooperative;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGeoZone() {
        return geoZone;
    }

    public void setGeoZone(String geoZone) {
        this.geoZone = geoZone;
    }

    public NationalCooperativeDTO getNationalCooperative() {
        return nationalCooperative;
    }

    public void setNationalCooperative(NationalCooperativeDTO nationalCooperative) {
        this.nationalCooperative = nationalCooperative;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocalCooperativeDTO)) {
            return false;
        }

        LocalCooperativeDTO localCooperativeDTO = (LocalCooperativeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, localCooperativeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocalCooperativeDTO{" +
            "id=" + getId() +
            ", geoZone='" + getGeoZone() + "'" +
            ", nationalCooperative=" + getNationalCooperative() +
            "}";
    }
}
