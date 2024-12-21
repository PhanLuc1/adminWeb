package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Attribute.
 */
@Entity
@Table(name = "attribute")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Attribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "create_at")
    private Instant createAt;

    @Column(name = "update_at")
    private Instant updateAt;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "attribute")
    @JsonIgnoreProperties(value = { "attribute" }, allowSetters = true)
    private Set<AttributeValue> attributeValues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Attribute id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreateAt() {
        return this.createAt;
    }

    public Attribute createAt(Instant createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    public Attribute updateAt(Instant updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public String getName() {
        return this.name;
    }

    public Attribute name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<AttributeValue> getAttributeValues() {
        return this.attributeValues;
    }

    public void setAttributeValues(Set<AttributeValue> attributeValues) {
        if (this.attributeValues != null) {
            this.attributeValues.forEach(i -> i.setAttribute(null));
        }
        if (attributeValues != null) {
            attributeValues.forEach(i -> i.setAttribute(this));
        }
        this.attributeValues = attributeValues;
    }

    public Attribute attributeValues(Set<AttributeValue> attributeValues) {
        this.setAttributeValues(attributeValues);
        return this;
    }

    public Attribute addAttributeValue(AttributeValue attributeValue) {
        this.attributeValues.add(attributeValue);
        attributeValue.setAttribute(this);
        return this;
    }

    public Attribute removeAttributeValue(AttributeValue attributeValue) {
        this.attributeValues.remove(attributeValue);
        attributeValue.setAttribute(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attribute)) {
            return false;
        }
        return getId() != null && getId().equals(((Attribute) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Attribute{" +
            "id=" + getId() +
            ", createAt='" + getCreateAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
