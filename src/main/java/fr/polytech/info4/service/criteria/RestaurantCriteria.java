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
 * Criteria class for the {@link fr.polytech.info4.domain.Restaurant} entity. This class is used
 * in {@link fr.polytech.info4.web.rest.RestaurantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /restaurants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RestaurantCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter address;

    private LongFilter userId;

    private LongFilter localCooperativeId;

    private LongFilter dishId;

    private LongFilter commandId;

    public RestaurantCriteria() {}

    public RestaurantCriteria(RestaurantCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.localCooperativeId = other.localCooperativeId == null ? null : other.localCooperativeId.copy();
        this.dishId = other.dishId == null ? null : other.dishId.copy();
        this.commandId = other.commandId == null ? null : other.commandId.copy();
    }

    @Override
    public RestaurantCriteria copy() {
        return new RestaurantCriteria(this);
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

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
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

    public LongFilter getLocalCooperativeId() {
        return localCooperativeId;
    }

    public LongFilter localCooperativeId() {
        if (localCooperativeId == null) {
            localCooperativeId = new LongFilter();
        }
        return localCooperativeId;
    }

    public void setLocalCooperativeId(LongFilter localCooperativeId) {
        this.localCooperativeId = localCooperativeId;
    }

    public LongFilter getDishId() {
        return dishId;
    }

    public LongFilter dishId() {
        if (dishId == null) {
            dishId = new LongFilter();
        }
        return dishId;
    }

    public void setDishId(LongFilter dishId) {
        this.dishId = dishId;
    }

    public LongFilter getCommandId() {
        return commandId;
    }

    public LongFilter commandId() {
        if (commandId == null) {
            commandId = new LongFilter();
        }
        return commandId;
    }

    public void setCommandId(LongFilter commandId) {
        this.commandId = commandId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RestaurantCriteria that = (RestaurantCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(address, that.address) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(localCooperativeId, that.localCooperativeId) &&
            Objects.equals(dishId, that.dishId) &&
            Objects.equals(commandId, that.commandId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, userId, localCooperativeId, dishId, commandId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RestaurantCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (localCooperativeId != null ? "localCooperativeId=" + localCooperativeId + ", " : "") +
            (dishId != null ? "dishId=" + dishId + ", " : "") +
            (commandId != null ? "commandId=" + commandId + ", " : "") +
            "}";
    }
}
