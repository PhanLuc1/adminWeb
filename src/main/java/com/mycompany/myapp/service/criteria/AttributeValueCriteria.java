package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.AttributeValue} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.AttributeValueResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /attribute-values?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttributeValueCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creatAt;

    private InstantFilter updateAt;

    private StringFilter name;

    private LongFilter attributeId;

    private Boolean distinct;

    public AttributeValueCriteria() {}

    public AttributeValueCriteria(AttributeValueCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.creatAt = other.optionalCreatAt().map(InstantFilter::copy).orElse(null);
        this.updateAt = other.optionalUpdateAt().map(InstantFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.attributeId = other.optionalAttributeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AttributeValueCriteria copy() {
        return new AttributeValueCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getCreatAt() {
        return creatAt;
    }

    public Optional<InstantFilter> optionalCreatAt() {
        return Optional.ofNullable(creatAt);
    }

    public InstantFilter creatAt() {
        if (creatAt == null) {
            setCreatAt(new InstantFilter());
        }
        return creatAt;
    }

    public void setCreatAt(InstantFilter creatAt) {
        this.creatAt = creatAt;
    }

    public InstantFilter getUpdateAt() {
        return updateAt;
    }

    public Optional<InstantFilter> optionalUpdateAt() {
        return Optional.ofNullable(updateAt);
    }

    public InstantFilter updateAt() {
        if (updateAt == null) {
            setUpdateAt(new InstantFilter());
        }
        return updateAt;
    }

    public void setUpdateAt(InstantFilter updateAt) {
        this.updateAt = updateAt;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getAttributeId() {
        return attributeId;
    }

    public Optional<LongFilter> optionalAttributeId() {
        return Optional.ofNullable(attributeId);
    }

    public LongFilter attributeId() {
        if (attributeId == null) {
            setAttributeId(new LongFilter());
        }
        return attributeId;
    }

    public void setAttributeId(LongFilter attributeId) {
        this.attributeId = attributeId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AttributeValueCriteria that = (AttributeValueCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creatAt, that.creatAt) &&
            Objects.equals(updateAt, that.updateAt) &&
            Objects.equals(name, that.name) &&
            Objects.equals(attributeId, that.attributeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creatAt, updateAt, name, attributeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttributeValueCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCreatAt().map(f -> "creatAt=" + f + ", ").orElse("") +
            optionalUpdateAt().map(f -> "updateAt=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalAttributeId().map(f -> "attributeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
