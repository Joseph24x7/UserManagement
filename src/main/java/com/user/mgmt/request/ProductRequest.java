package com.user.mgmt.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductRequest {
    private String name;
    private double price;
    private MultipartFile image;
}