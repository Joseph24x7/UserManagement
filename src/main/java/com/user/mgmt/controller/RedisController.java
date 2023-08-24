package com.user.mgmt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/delete/{key}")
    public ResponseEntity<String> delete(@PathVariable String key) {
        if (Boolean.TRUE.equals(redisOperations.delete(key))) {
            return ResponseEntity.ok("Key-value pair deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
