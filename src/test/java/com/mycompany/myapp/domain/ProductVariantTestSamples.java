package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ProductVariantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProductVariant getProductVariantSample1() {
        return new ProductVariant().id(1L);
    }

    public static ProductVariant getProductVariantSample2() {
        return new ProductVariant().id(2L);
    }

    public static ProductVariant getProductVariantRandomSampleGenerator() {
        return new ProductVariant().id(longCount.incrementAndGet());
    }
}
