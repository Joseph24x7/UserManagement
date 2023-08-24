package com.user.mgmt.IT;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RedisControllerIT implements RedisContainerInitializer {

    @Autowired
    private TestRestTemplate restTemplate;
    private final String key = "testKey";
    private final String value = "testValue";

    @Test
    @Order(0)
    public void testCreate() {
        ResponseEntity<String> createResponse = restTemplate.postForEntity("/redis/create?key=" + key + "&value=" + value, null, String.class);
        assertEquals(200, createResponse.getStatusCode().value());
    }

    @Test
    @Order(1)
    public void testRead() {
        ResponseEntity<String> readResponse = restTemplate.getForEntity("/redis/read/" + key, String.class);
        assertEquals(200, readResponse.getStatusCode().value());
        assertEquals(value, readResponse.getBody());
    }

    @Test
    @Order(2)
    public void testDelete() {
        restTemplate.postForEntity("/redis/create?key=" + key + "&value=" + value, null, String.class);
        ResponseEntity<String> deleteResponse = restTemplate.exchange("/redis/delete/" + key, org.springframework.http.HttpMethod.DELETE, null, String.class);
        assertEquals(200, deleteResponse.getStatusCode().value());
    }
}
