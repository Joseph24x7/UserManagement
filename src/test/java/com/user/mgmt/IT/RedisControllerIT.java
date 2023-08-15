package com.user.mgmt.IT;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RedisControllerIT implements RedisContainerInitializer {

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public void setup() {
        restTemplate.exchange("/redis/delete", HttpMethod.DELETE, null, String.class);
    }

    @Test
    public void testCreateAndRead() {
        String key = "testKey";
        String value = "testValue";

        ResponseEntity<String> createResponse = restTemplate.postForEntity("/redis/create?key=" + key + "&value=" + value, null, String.class);
        assertEquals(200, createResponse.getStatusCode().value());

        ResponseEntity<String> readResponse = restTemplate.getForEntity("/redis/read/" + key, String.class);
        assertEquals(200, readResponse.getStatusCode().value());
        assertEquals(value, readResponse.getBody());
    }

    @Test
    public void testReadAll() {
        ResponseEntity<Map<String, String>> readAllResponse = restTemplate.exchange("/redis/readAll", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
        assertEquals(200, readAllResponse.getStatusCode().value());
        assertNotNull(readAllResponse.getBody());
        assertEquals("{testKey=testValue}", readAllResponse.getBody().toString());
    }

    @Test
    public void testDelete() {
        String key = "testKey";
        String value = "testValue";

        restTemplate.postForEntity("/redis/create?key=" + key + "&value=" + value, null, String.class);

        ResponseEntity<String> deleteResponse = restTemplate.exchange("/redis/delete/" + key, org.springframework.http.HttpMethod.DELETE, null, String.class);
        assertEquals(200, deleteResponse.getStatusCode().value());
    }
}
