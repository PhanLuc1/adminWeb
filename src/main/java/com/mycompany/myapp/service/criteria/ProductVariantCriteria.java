package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.ProductVariant} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ProductVariantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-variants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductVariantCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creatAt;

    private InstantFilter updateAt;

    private BigDecimalFilter price;

    private LongFilter attributeValueId;

    private Boolean distinct;

    public ProductVariantCriteria() {}

    public ProductVariantCriteria(ProductVariantCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.creatAt = other.optionalCreatAt().map(InstantFilter::copy).orElse(null);
        this.updateAt = other.optionalUpdateAt().map(InstantFilter::copy).orElse(null);
        this.price = other.optionalPrice().map(BigDecimalFilter::copy).orElse(null);
        this.attributeValueId = other.optionalAttributeValueId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductVariantCriteria copy() {
        return new ProductVariantCriteria(this);
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

    public BigDecimalFilter getPrice() {
        return price;
    }

    public Optional<BigDecimalFilter> optionalPrice() {
        return Optional.ofNullable(price);
    }

    public BigDecimalFilter price() {
        if (price == null) {
            setPrice(new BigDecimalFilter());
        }
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public LongFilter getAttributeValueId() {
        return attributeValueId;
    }

    public Optional<LongFilter> optionalAttributeValueId() {
        return Optional.ofNullable(attributeValueId);
    }

    public LongFilter attributeValueId() {
        if (attributeValueId == null) {
            setAttributeValueId(new LongFilter());
        }
        return attributeValueId;
    }

    public void setAttributeValueId(LongFilter attributeValueId) {
        this.attributeValueId = attributeValueId;
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
        final ProductVariantCriteria that = (ProductVariantCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creatAt, that.creatAt) &&
            Objects.equals(updateAt, that.updateAt) &&
            Objects.equals(price, that.price) &&
            Objects.equals(attributeValueId, that.attributeValueId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creatAt, updateAt, price, attributeValueId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductVariantCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCreatAt().map(f -> "creatAt=" + f + ", ").orElse("") +
            optionalUpdateAt().map(f -> "updateAt=" + f + ", ").orElse("") +
            optionalPrice().map(f -> "price=" + f + ", ").orElse("") +
            optionalAttributeValueId().map(f -> "attributeValueId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
