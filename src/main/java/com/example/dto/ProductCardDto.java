package com.example.dto;

import lombok.Data;

@Data
public class ProductCardDto {
    private String productId;
    
    private String productName;
    
    private int price;
    
    private boolean outOfStock; // stock == 0 で true
    
    private String thumbnailUrl;
}
