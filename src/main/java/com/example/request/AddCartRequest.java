package com.example.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import lombok.Data;

@Data
public class AddCartRequest {
    
    private String productId;

    @Min(1)
    @Max(20)
    private int qty;
}
