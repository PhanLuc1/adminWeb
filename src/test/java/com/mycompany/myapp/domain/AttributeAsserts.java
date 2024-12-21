package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AttributeAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAttributeAllPropertiesEquals(Attribute expected, Attribute actual) {
        assertAttributeAutoGeneratedPropertiesEquals(expected, actual);
        assertAttributeAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAttributeAllUpdatablePropertiesEquals(Attribute expected, Attribute actual) {
        assertAttributeUpdatableFieldsEquals(expected, actual);
        assertAttributeUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAttributeAutoGeneratedPropertiesEquals(Attribute expected, Attribute actual) {
        assertThat(expected)
            .as("Verify Attribute auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAttributeUpdatableFieldsEquals(Attribute expected, Attribute actual) {
        assertThat(expected)
            .as("Verify Attribute relevant properties")
            .satisfies(e -> assertThat(e.getCreateAt()).as("check createAt").isEqualTo(actual.getCreateAt()))
            .satisfies(e -> assertThat(e.getUpdateAt()).as("check updateAt").isEqualTo(actual.getUpdateAt()))
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAttributeUpdatableRelationshipsEquals(Attribute expected, Attribute actual) {
        // empty method
    }
}
