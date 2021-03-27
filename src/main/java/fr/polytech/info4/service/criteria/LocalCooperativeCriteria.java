package fr.polytech.info4.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link fr.polytech.info4.domain.LocalCooperative} entity. This class is used
 * in {@link fr.polytech.info4.web.rest.LocalCooperativeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /local-cooperatives?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LocalCooperativeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter geoZone;

    private LongFilter nationalCooperativeId;

    private LongFilter restaurantId;

    public LocalCooperativeCriteria() {}

    public LocalCooperativeCriteria(LocalCooperativeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.geoZone = other.geoZone == null ? null : other.geoZone.copy();
        this.nationalCooperativeId = other.nationalCooperativeId == null ? null : other.nationalCooperativeId.copy();
        this.restaurantId = other.restaurantId == null ? null : other.restaurantId.copy();
    }

    @Override
    public LocalCooperativeCriteria copy() {
        return new LocalCooperativeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getGeoZone() {
        return geoZone;
    }

    public StringFilter geoZone() {
        if (geoZone == null) {
            geoZone = new StringFilter();
        }
        return geoZone;
    }

    public void setGeoZone(StringFilter geoZone) {
        this.geoZone = geoZone;
    }

    public LongFilter getNationalCooperativeId() {
        return nationalCooperativeId;
    }

    public LongFilter nationalCooperativeId() {
        if (nationalCooperativeId == null) {
            nationalCooperativeId = new LongFilter();
        }
        return nationalCooperativeId;
    }

    public void setNationalCooperativeId(LongFilter nationalCooperativeId) {
        this.nationalCooperativeId = nationalCooperativeId;
    }

    public LongFilter getRestaurantId() {
        return restaurantId;
    }

    public LongFilter restaurantId() {
        if (restaurantId == null) {
            restaurantId = new LongFilter();
        }
        return restaurantId;
    }

    public void setRestaurantId(LongFilter restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LocalCooperativeCriteria that = (LocalCooperativeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(geoZone, that.geoZone) &&
            Objects.equals(nationalCooperativeId, that.nationalCooperativeId) &&
            Objects.equals(restaurantId, that.restaurantId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, geoZone, nationalCooperativeId, restaurantId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocalCooperativeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (geoZone != null ? "geoZone=" + geoZone + ", " : "") +
            (nationalCooperativeId != null ? "nationalCooperativeId=" + nationalCooperativeId + ", " : "") +
            (restaurantId != null ? "restaurantId=" + restaurantId + ", " : "") +
            "}";
    }
}
