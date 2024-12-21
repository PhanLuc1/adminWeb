package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AttributeCriteriaTest {

    @Test
    void newAttributeCriteriaHasAllFiltersNullTest() {
        var attributeCriteria = new AttributeCriteria();
        assertThat(attributeCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void attributeCriteriaFluentMethodsCreatesFiltersTest() {
        var attributeCriteria = new AttributeCriteria();

        setAllFilters(attributeCriteria);

        assertThat(attributeCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void attributeCriteriaCopyCreatesNullFilterTest() {
        var attributeCriteria = new AttributeCriteria();
        var copy = attributeCriteria.copy();

        assertThat(attributeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(attributeCriteria)
        );
    }

    @Test
    void attributeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var attributeCriteria = new AttributeCriteria();
        setAllFilters(attributeCriteria);

        var copy = attributeCriteria.copy();

        assertThat(attributeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(attributeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var attributeCriteria = new AttributeCriteria();

        assertThat(attributeCriteria).hasToString("AttributeCriteria{}");
    }

    private static void setAllFilters(AttributeCriteria attributeCriteria) {
        attributeCriteria.id();
        attributeCriteria.createAt();
        attributeCriteria.updateAt();
        attributeCriteria.name();
        attributeCriteria.distinct();
    }

    private static Condition<AttributeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCreateAt()) &&
                condition.apply(criteria.getUpdateAt()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AttributeCriteria> copyFiltersAre(AttributeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCreateAt(), copy.getCreateAt()) &&
                condition.apply(criteria.getUpdateAt(), copy.getUpdateAt()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
