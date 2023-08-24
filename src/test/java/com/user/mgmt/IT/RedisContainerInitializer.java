package com.user.mgmt.IT;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.Map;
import java.util.function.Supplier;

public interface RedisContainerInitializer {

    Map<String, Supplier<Object>> DYNAMIC_PROPERTIES = IntegrationTestBase.Initializer.initialize();

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        DYNAMIC_PROPERTIES.forEach(dynamicPropertyRegistry::add);
    }

}
