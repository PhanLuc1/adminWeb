package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Attribute} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.AttributeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /attributes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttributeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter createAt;

    private InstantFilter updateAt;

    private StringFilter name;

    private Boolean distinct;

    public AttributeCriteria() {}

    public AttributeCriteria(AttributeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.createAt = other.optionalCreateAt().map(InstantFilter::copy).orElse(null);
        this.updateAt = other.optionalUpdateAt().map(InstantFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AttributeCriteria copy() {
        return new AttributeCriteria(this);
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

    public InstantFilter getCreateAt() {
        return createAt;
    }

    public Optional<InstantFilter> optionalCreateAt() {
        return Optional.ofNullable(createAt);
    }

    public InstantFilter createAt() {
        if (createAt == null) {
            setCreateAt(new InstantFilter());
        }
        return createAt;
    }

    public void setCreateAt(InstantFilter createAt) {
        this.createAt = createAt;
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
        final AttributeCriteria that = (AttributeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createAt, that.createAt) &&
            Objects.equals(updateAt, that.updateAt) &&
            Objects.equals(name, that.name) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createAt, updateAt, name, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttributeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCreateAt().map(f -> "createAt=" + f + ", ").orElse("") +
            optionalUpdateAt().map(f -> "updateAt=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
