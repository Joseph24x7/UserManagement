package com.user.mgmt.controller;

import com.user.mgmt.entity.ProductEntity;
import com.user.mgmt.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product-list")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<ProductEntity>> getProductList() {
        List<ProductEntity> products = productRepository.findAll();
        return ResponseEntity.ok(products);
    }

}