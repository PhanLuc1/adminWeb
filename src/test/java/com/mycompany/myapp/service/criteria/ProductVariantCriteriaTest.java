package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductVariantCriteriaTest {

    @Test
    void newProductVariantCriteriaHasAllFiltersNullTest() {
        var productVariantCriteria = new ProductVariantCriteria();
        assertThat(productVariantCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void productVariantCriteriaFluentMethodsCreatesFiltersTest() {
        var productVariantCriteria = new ProductVariantCriteria();

        setAllFilters(productVariantCriteria);

        assertThat(productVariantCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void productVariantCriteriaCopyCreatesNullFilterTest() {
        var productVariantCriteria = new ProductVariantCriteria();
        var copy = productVariantCriteria.copy();

        assertThat(productVariantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(productVariantCriteria)
        );
    }

    @Test
    void productVariantCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productVariantCriteria = new ProductVariantCriteria();
        setAllFilters(productVariantCriteria);

        var copy = productVariantCriteria.copy();

        assertThat(productVariantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(productVariantCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productVariantCriteria = new ProductVariantCriteria();

        assertThat(productVariantCriteria).hasToString("ProductVariantCriteria{}");
    }

    private static void setAllFilters(ProductVariantCriteria productVariantCriteria) {
        productVariantCriteria.id();
        productVariantCriteria.creatAt();
        productVariantCriteria.updateAt();
        productVariantCriteria.price();
        productVariantCriteria.attributeValueId();
        productVariantCriteria.distinct();
    }

    private static Condition<ProductVariantCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCreatAt()) &&
                condition.apply(criteria.getUpdateAt()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getAttributeValueId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductVariantCriteria> copyFiltersAre(
        ProductVariantCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCreatAt(), copy.getCreatAt()) &&
                condition.apply(criteria.getUpdateAt(), copy.getUpdateAt()) &&
                condition.apply(criteria.getPrice(), copy.getPrice()) &&
                condition.apply(criteria.getAttributeValueId(), copy.getAttributeValueId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
