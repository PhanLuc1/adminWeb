package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AttributeValueAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAttributeValueAllPropertiesEquals(AttributeValue expected, AttributeValue actual) {
        assertAttributeValueAutoGeneratedPropertiesEquals(expected, actual);
        assertAttributeValueAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAttributeValueAllUpdatablePropertiesEquals(AttributeValue expected, AttributeValue actual) {
        assertAttributeValueUpdatableFieldsEquals(expected, actual);
        assertAttributeValueUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAttributeValueAutoGeneratedPropertiesEquals(AttributeValue expected, AttributeValue actual) {
        assertThat(expected)
            .as("Verify AttributeValue auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAttributeValueUpdatableFieldsEquals(AttributeValue expected, AttributeValue actual) {
        assertThat(expected)
            .as("Verify AttributeValue relevant properties")
            .satisfies(e -> assertThat(e.getCreatAt()).as("check creatAt").isEqualTo(actual.getCreatAt()))
            .satisfies(e -> assertThat(e.getUpdateAt()).as("check updateAt").isEqualTo(actual.getUpdateAt()))
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAttributeValueUpdatableRelationshipsEquals(AttributeValue expected, AttributeValue actual) {
        assertThat(expected)
            .as("Verify AttributeValue relationships")
            .satisfies(e -> assertThat(e.getAttribute()).as("check attribute").isEqualTo(actual.getAttribute()))
            .satisfies(e -> assertThat(e.getProductVariants()).as("check productVariants").isEqualTo(actual.getProductVariants()));
    }
}
