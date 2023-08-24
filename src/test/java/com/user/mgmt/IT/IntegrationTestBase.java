package com.user.mgmt.IT;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTestBase {

    public static RedisContainer redisContainer = new RedisContainer(DockerImageName.parse("redis:7.0.11")).withExposedPorts(6379);

    public static class Initializer {

        public static Map<String, Supplier<Object>> initialize() {
            Stream.of(redisContainer).forEach(GenericContainer::start);
            Map<String, Supplier<Object>> appProperties = new HashMap<>();
            appProperties.put("spring.data.redis.port", () -> redisContainer.getFirstMappedPort());
            appProperties.put("spring.data.redis.host", () -> redisContainer.getHost());
            return appProperties;
        }

    }
}
