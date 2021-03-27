package fr.polytech.info4.service.criteria;

import fr.polytech.info4.domain.enumeration.STATECMD;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link fr.polytech.info4.domain.Command} entity. This class is used
 * in {@link fr.polytech.info4.web.rest.CommandResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /commands?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CommandCriteria implements Serializable, Criteria {

    /**
     * Class for filtering STATECMD
     */
    public static class STATECMDFilter extends Filter<STATECMD> {

        public STATECMDFilter() {}

        public STATECMDFilter(STATECMDFilter filter) {
            super(filter);
        }

        @Override
        public STATECMDFilter copy() {
            return new STATECMDFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter date;

    private FloatFilter price;

    private STATECMDFilter state;

    private LongFilter clientId;

    private LongFilter deliveryId;

    private LongFilter restaurantId;

    private LongFilter dishId;

    public CommandCriteria() {}

    public CommandCriteria(CommandCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.clientId = other.clientId == null ? null : other.clientId.copy();
        this.deliveryId = other.deliveryId == null ? null : other.deliveryId.copy();
        this.restaurantId = other.restaurantId == null ? null : other.restaurantId.copy();
        this.dishId = other.dishId == null ? null : other.dishId.copy();
    }

    @Override
    public CommandCriteria copy() {
        return new CommandCriteria(this);
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

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public FloatFilter getPrice() {
        return price;
    }

    public FloatFilter price() {
        if (price == null) {
            price = new FloatFilter();
        }
        return price;
    }

    public void setPrice(FloatFilter price) {
        this.price = price;
    }

    public STATECMDFilter getState() {
        return state;
    }

    public STATECMDFilter state() {
        if (state == null) {
            state = new STATECMDFilter();
        }
        return state;
    }

    public void setState(STATECMDFilter state) {
        this.state = state;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public LongFilter clientId() {
        if (clientId == null) {
            clientId = new LongFilter();
        }
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CommandCriteria that = (CommandCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(price, that.price) &&
            Objects.equals(state, that.state) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(deliveryId, that.deliveryId) &&
            Objects.equals(restaurantId, that.restaurantId) &&
            Objects.equals(dishId, that.dishId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, price, state, clientId, deliveryId, restaurantId, dishId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommandCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (state != null ? "state=" + state + ", " : "") +
            (clientId != null ? "clientId=" + clientId + ", " : "") +
            (deliveryId != null ? "deliveryId=" + deliveryId + ", " : "") +
            (restaurantId != null ? "restaurantId=" + restaurantId + ", " : "") +
            (dishId != null ? "dishId=" + dishId + ", " : "") +
            "}";
    }
}
