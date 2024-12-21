package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ProductVariant} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductVariantDTO implements Serializable {

    private Long id;

    private Instant creatAt;

    private Instant updateAt;

    private BigDecimal price;

    private Set<AttributeValueDTO> attributeValues = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatAt() {
        return creatAt;
    }

    public void setCreatAt(Instant creatAt) {
        this.creatAt = creatAt;
    }

    public Instant getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Set<AttributeValueDTO> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(Set<AttributeValueDTO> attributeValues) {
        this.attributeValues = attributeValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductVariantDTO)) {
            return false;
        }

        ProductVariantDTO productVariantDTO = (ProductVariantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productVariantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductVariantDTO{" +
            "id=" + getId() +
            ", creatAt='" + getCreatAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", price=" + getPrice() +
            ", attributeValues=" + getAttributeValues() +
            "}";
    }
}
