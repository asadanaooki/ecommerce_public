package com.example.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
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
