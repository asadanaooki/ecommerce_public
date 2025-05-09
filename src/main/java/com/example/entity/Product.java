package com.example.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Product {

    private String productId;
    
    private String productName;
    
    private int price;
    
    private int stock;
    
    private String saleStatus;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
