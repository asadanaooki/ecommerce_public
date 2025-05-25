package com.example.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductDetailDto {
    private String productId;
    
    private String productName;
    
    private String productDescription;
    
    private int price;
    
    private boolean outOfStock;
    
    private BigDecimal ratingAvg;
    
    private int reviewCount;
    
    private boolean isFav;
}
