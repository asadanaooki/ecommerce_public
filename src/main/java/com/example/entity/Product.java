package com.example.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Product {
    // TODO: 定数をこのクラスに定義するべきか→Enumがいいのか？
    public static final String SALE_OFF = "0";
    
    public static final String SALE_ON = "1";

    private String productId;
    
    private String productName;
    
    private int price;
    
    private int stock;
    
    private String saleStatus;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
