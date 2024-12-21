package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AttributeValueCriteriaTest {

    @Test
    void newAttributeValueCriteriaHasAllFiltersNullTest() {
        var attributeValueCriteria = new AttributeValueCriteria();
        assertThat(attributeValueCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void attributeValueCriteriaFluentMethodsCreatesFiltersTest() {
        var attributeValueCriteria = new AttributeValueCriteria();

        setAllFilters(attributeValueCriteria);

        assertThat(attributeValueCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void attributeValueCriteriaCopyCreatesNullFilterTest() {
        var attributeValueCriteria = new AttributeValueCriteria();
        var copy = attributeValueCriteria.copy();

        assertThat(attributeValueCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(attributeValueCriteria)
        );
    }

    @Test
    void attributeValueCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var attributeValueCriteria = new AttributeValueCriteria();
        setAllFilters(attributeValueCriteria);

        var copy = attributeValueCriteria.copy();

        assertThat(attributeValueCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(attributeValueCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var attributeValueCriteria = new AttributeValueCriteria();

        assertThat(attributeValueCriteria).hasToString("AttributeValueCriteria{}");
    }

    private static void setAllFilters(AttributeValueCriteria attributeValueCriteria) {
        attributeValueCriteria.id();
        attributeValueCriteria.creatAt();
        attributeValueCriteria.updateAt();
        attributeValueCriteria.name();
        attributeValueCriteria.attributeId();
        attributeValueCriteria.productVariantId();
        attributeValueCriteria.distinct();
    }

    private static Condition<AttributeValueCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCreatAt()) &&
                condition.apply(criteria.getUpdateAt()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getAttributeId()) &&
                condition.apply(criteria.getProductVariantId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AttributeValueCriteria> copyFiltersAre(
        AttributeValueCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCreatAt(), copy.getCreatAt()) &&
                condition.apply(criteria.getUpdateAt(), copy.getUpdateAt()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getAttributeId(), copy.getAttributeId()) &&
                condition.apply(criteria.getProductVariantId(), copy.getProductVariantId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
