package com.user.mgmt.entity;

import jakarta.persistence.*;
import lombok.Data;

import lombok.Data;

@Data
@Entity
@Table(name = "Product_Info", schema = "myapp")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private Long id;

    @Lob
    @Column(name = "product_image", columnDefinition = "bytea")
    private byte[] image;

    @Column(name = "product_name")
    private String name;

    @Column(name = "product_price")
    private double price;

}

