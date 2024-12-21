package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A ProductVariant.
 */
@Entity
@Table(name = "product_variant")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductVariant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "creat_at")
    private Instant creatAt;

    @Column(name = "update_at")
    private Instant updateAt;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_product_variant__attribute_value",
        joinColumns = @JoinColumn(name = "product_variant_id"),
        inverseJoinColumns = @JoinColumn(name = "attribute_value_id")
    )
    @JsonIgnoreProperties(value = { "attribute", "productVariants" }, allowSetters = true)
    private Set<AttributeValue> attributeValues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductVariant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatAt() {
        return this.creatAt;
    }

    public ProductVariant creatAt(Instant creatAt) {
        this.setCreatAt(creatAt);
        return this;
    }

    public void setCreatAt(Instant creatAt) {
        this.creatAt = creatAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    public ProductVariant updateAt(Instant updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public ProductVariant price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Set<AttributeValue> getAttributeValues() {
        return this.attributeValues;
    }

    public void setAttributeValues(Set<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
    }

    public ProductVariant attributeValues(Set<AttributeValue> attributeValues) {
        this.setAttributeValues(attributeValues);
        return this;
    }

    public ProductVariant addAttributeValue(AttributeValue attributeValue) {
        this.attributeValues.add(attributeValue);
        return this;
    }

    public ProductVariant removeAttributeValue(AttributeValue attributeValue) {
        this.attributeValues.remove(attributeValue);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductVariant)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductVariant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductVariant{" +
            "id=" + getId() +
            ", creatAt='" + getCreatAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
