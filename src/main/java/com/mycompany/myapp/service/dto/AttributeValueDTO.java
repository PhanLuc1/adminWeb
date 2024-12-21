package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.AttributeValue} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttributeValueDTO implements Serializable {

    private Long id;

    private Instant creatAt;

    private Instant updateAt;

    private String name;

    private AttributeDTO attribute;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AttributeDTO getAttribute() {
        return attribute;
    }

    public void setAttribute(AttributeDTO attribute) {
        this.attribute = attribute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttributeValueDTO)) {
            return false;
        }

        AttributeValueDTO attributeValueDTO = (AttributeValueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, attributeValueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttributeValueDTO{" +
            "id=" + getId() +
            ", creatAt='" + getCreatAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", name='" + getName() + "'" +
            ", attribute=" + getAttribute() +
            "}";
    }
}
