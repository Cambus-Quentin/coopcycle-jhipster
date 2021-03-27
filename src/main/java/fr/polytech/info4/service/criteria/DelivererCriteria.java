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
 * Criteria class for the {@link fr.polytech.info4.domain.Deliverer} entity. This class is used
 * in {@link fr.polytech.info4.web.rest.DelivererResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /deliverers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DelivererCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter firstname;

    private LongFilter userId;

    private LongFilter deliveryId;

    public DelivererCriteria() {}

    public DelivererCriteria(DelivererCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.firstname = other.firstname == null ? null : other.firstname.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.deliveryId = other.deliveryId == null ? null : other.deliveryId.copy();
    }

    @Override
    public DelivererCriteria copy() {
        return new DelivererCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getFirstname() {
        return firstname;
    }

    public StringFilter firstname() {
        if (firstname == null) {
            firstname = new StringFilter();
        }
        return firstname;
    }

    public void setFirstname(StringFilter firstname) {
        this.firstname = firstname;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getDeliveryId() {
        return deliveryId;
    }

    public LongFilter deliveryId() {
        if (deliveryId == null) {
            deliveryId = new LongFilter();
        }
        return deliveryId;
    }

    public void setDeliveryId(LongFilter deliveryId) {
        this.deliveryId = deliveryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DelivererCriteria that = (DelivererCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(firstname, that.firstname) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(deliveryId, that.deliveryId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, firstname, userId, deliveryId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DelivererCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (firstname != null ? "firstname=" + firstname + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (deliveryId != null ? "deliveryId=" + deliveryId + ", " : "") +
            "}";
    }
}
