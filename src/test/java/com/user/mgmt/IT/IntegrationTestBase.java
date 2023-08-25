package com.user.mgmt.IT;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IntegrationTestBase {

    private static final Map<String, Supplier<Object>> DYNAMIC_PROPERTIES = IntegrationTestBase.Initializer.initialize();

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        DYNAMIC_PROPERTIES.forEach(dynamicPropertyRegistry::add);
    }

    public static class Initializer {

        private static final RedisContainer redisContainer = new RedisContainer(DockerImageName.parse("redis:7.0.11"))
                .withExposedPorts(6379);

        private static final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                .withDatabaseName("myuser")
                .withUsername("myuser")
                .withPassword("password")
                .withInitScript("init.sql");

        public static Map<String, Supplier<Object>> initialize() {
            Stream.of(redisContainer, postgresqlContainer).forEach(GenericContainer::start);
            Map<String, Supplier<Object>> appProperties = new HashMap<>();
            appProperties.put("spring.data.redis.port", redisContainer::getFirstMappedPort);
            appProperties.put("spring.data.redis.host", redisContainer::getHost);
            appProperties.put("spring.datasource.url", postgresqlContainer::getJdbcUrl);
            appProperties.put("spring.datasource.username", postgresqlContainer::getUsername);
            appProperties.put("spring.datasource.password", postgresqlContainer::getPassword);
            return appProperties;
        }

    }
}
