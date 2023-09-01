package com.user.mgmt.IT;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RedisControllerIT extends IntegrationTestBase {

    @Autowired
    private WebTestClient webTestClient;
    private final String key = "testKey";
    private final String value = "testValue";

    @Test
    @Order(0)
    public void testCreate() {
        webTestClient.post()
                .uri("/redis/create?key=" + key + "&value=" + value)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @Order(1)
    public void testRead() {
        webTestClient.get()
                .uri("/redis/read/" + key)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(value);
    }

    @Test
    @Order(2)
    public void testDelete() {

        webTestClient.delete()
                .uri("/redis/delete/" + key)
                .exchange()
                .expectStatus().isOk();
    }
}
