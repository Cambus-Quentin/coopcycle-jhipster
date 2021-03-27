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
 * Criteria class for the {@link fr.polytech.info4.domain.NationalCooperative} entity. This class is used
 * in {@link fr.polytech.info4.web.rest.NationalCooperativeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /national-cooperatives?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NationalCooperativeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter localCooperativeId;

    public NationalCooperativeCriteria() {}

    public NationalCooperativeCriteria(NationalCooperativeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.localCooperativeId = other.localCooperativeId == null ? null : other.localCooperativeId.copy();
    }

    @Override
    public NationalCooperativeCriteria copy() {
        return new NationalCooperativeCriteria(this);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NationalCooperativeCriteria that = (NationalCooperativeCriteria) o;
        return (
            Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(localCooperativeId, that.localCooperativeId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, localCooperativeId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NationalCooperativeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (localCooperativeId != null ? "localCooperativeId=" + localCooperativeId + ", " : "") +
            "}";
    }
}
