package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A AttributeValue.
 */
@Entity
@Table(name = "attribute_value")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttributeValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "creat_at")
    private Instant creatAt;

    @Column(name = "update_at")
    private Instant updateAt;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "attributeValues" }, allowSetters = true)
    private Attribute attribute;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AttributeValue id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatAt() {
        return this.creatAt;
    }

    public AttributeValue creatAt(Instant creatAt) {
        this.setCreatAt(creatAt);
        return this;
    }

    public void setCreatAt(Instant creatAt) {
        this.creatAt = creatAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    public AttributeValue updateAt(Instant updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public String getName() {
        return this.name;
    }

    public AttributeValue name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Attribute getAttribute() {
        return this.attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public AttributeValue attribute(Attribute attribute) {
        this.setAttribute(attribute);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttributeValue)) {
            return false;
        }
        return getId() != null && getId().equals(((AttributeValue) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttributeValue{" +
            "id=" + getId() +
            ", creatAt='" + getCreatAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
