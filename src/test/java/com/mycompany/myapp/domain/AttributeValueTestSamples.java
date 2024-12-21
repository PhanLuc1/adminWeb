package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AttributeValueTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AttributeValue getAttributeValueSample1() {
        return new AttributeValue().id(1L).name("name1");
    }

    public static AttributeValue getAttributeValueSample2() {
        return new AttributeValue().id(2L).name("name2");
    }

    public static AttributeValue getAttributeValueRandomSampleGenerator() {
        return new AttributeValue().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
