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
 * Criteria class for the {@link fr.polytech.info4.domain.Delivery} entity. This class is used
 * in {@link fr.polytech.info4.web.rest.DeliveryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /deliveries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DeliveryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter deliveryAddr;

    private IntegerFilter distance;

    private FloatFilter price;

    private LongFilter delivererId;

    private LongFilter commandId;

    public DeliveryCriteria() {}

    public DeliveryCriteria(DeliveryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.deliveryAddr = other.deliveryAddr == null ? null : other.deliveryAddr.copy();
        this.distance = other.distance == null ? null : other.distance.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.delivererId = other.delivererId == null ? null : other.delivererId.copy();
        this.commandId = other.commandId == null ? null : other.commandId.copy();
    }

    @Override
    public DeliveryCriteria copy() {
        return new DeliveryCriteria(this);
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

    public StringFilter getDeliveryAddr() {
        return deliveryAddr;
    }

    public StringFilter deliveryAddr() {
        if (deliveryAddr == null) {
            deliveryAddr = new StringFilter();
        }
        return deliveryAddr;
    }

    public void setDeliveryAddr(StringFilter deliveryAddr) {
        this.deliveryAddr = deliveryAddr;
    }

    public IntegerFilter getDistance() {
        return distance;
    }

    public IntegerFilter distance() {
        if (distance == null) {
            distance = new IntegerFilter();
        }
        return distance;
    }

    public void setDistance(IntegerFilter distance) {
        this.distance = distance;
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

    public LongFilter getDelivererId() {
        return delivererId;
    }

    public LongFilter delivererId() {
        if (delivererId == null) {
            delivererId = new LongFilter();
        }
        return delivererId;
    }

    public void setDelivererId(LongFilter delivererId) {
        this.delivererId = delivererId;
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
        final DeliveryCriteria that = (DeliveryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(deliveryAddr, that.deliveryAddr) &&
            Objects.equals(distance, that.distance) &&
            Objects.equals(price, that.price) &&
            Objects.equals(delivererId, that.delivererId) &&
            Objects.equals(commandId, that.commandId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deliveryAddr, distance, price, delivererId, commandId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (deliveryAddr != null ? "deliveryAddr=" + deliveryAddr + ", " : "") +
            (distance != null ? "distance=" + distance + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (delivererId != null ? "delivererId=" + delivererId + ", " : "") +
            (commandId != null ? "commandId=" + commandId + ", " : "") +
            "}";
    }
}
