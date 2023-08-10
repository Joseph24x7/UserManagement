package com.user.mgmt.service;

import com.user.mgmt.entity.ProductEntity;
import com.user.mgmt.repository.ProductRepository;
import com.user.mgmt.request.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    public void addProduct(ProductRequest productRequest) throws IOException {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(productRequest.getName());
        productEntity.setPrice(productRequest.getPrice());
        productEntity.setImage(productRequest.getImage().getBytes());

        productRepository.save(productEntity);
    }
}
