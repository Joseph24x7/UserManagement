package com.user.mgmt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisController {

    private final RedisOperations<String, String> redisOperations;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestParam String key, @RequestParam String value) {
        redisOperations.opsForValue().set(key, value);
        return ResponseEntity.ok("Key-value pair created successfully");
    }

    @GetMapping("/read/{key}")
    public ResponseEntity<String> read(@PathVariable String key) {
        String value = redisOperations.opsForValue().get(key);
        if (value != null) {
            return ResponseEntity.ok(value);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/readAll")
    public ResponseEntity<Map<String, String>> readAll() {
        Set<String> keys = redisOperations.keys("*"); // Get all keys
        Map<String, String> keyValuePairs = new HashMap<>();

        for (String key : keys) {
            String value = redisOperations.opsForValue().get(key);
            keyValuePairs.put(key, value);
        }

        return ResponseEntity.ok(keyValuePairs);
    }

    @DeleteMapping("/delete/{key}")
    public ResponseEntity<String> delete(@PathVariable String key) {
        if (redisOperations.delete(key)) {
            return ResponseEntity.ok("Key-value pair deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete() {
        Set<String> keys = redisOperations.keys("*");
        if (redisOperations.delete(keys) > 0) {
            return ResponseEntity.ok("All the Key-value pairs deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
