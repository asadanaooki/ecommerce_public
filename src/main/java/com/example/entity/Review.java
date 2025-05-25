package com.example.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Review {

    private String userId;
    
    private String productId;
    
    private String comment;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
