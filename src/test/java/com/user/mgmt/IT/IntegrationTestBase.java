package com.user.mgmt.IT;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTestBase {

    public static RedisContainer redisContainer = new RedisContainer(DockerImageName.parse("redis:7.0.11")).withExposedPorts(6379);
    public static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("myuser")
            .withUsername("myuser")
            .withPassword("password");
    public static class Initializer {

        public static Map<String, Supplier<Object>> initialize() {
            Stream.of(redisContainer, postgresqlContainer).forEach(GenericContainer::start);
            Map<String, Supplier<Object>> appProperties = new HashMap<>();
            appProperties.put("spring.data.redis.port", () -> redisContainer.getFirstMappedPort());
            appProperties.put("spring.data.redis.host", () -> redisContainer.getHost());
            appProperties.put("spring.datasource.url", postgresqlContainer::getJdbcUrl);
            appProperties.put("spring.datasource.username", postgresqlContainer::getUsername);
            appProperties.put("spring.datasource.password", postgresqlContainer::getPassword);
            return appProperties;
        }

    }
}
