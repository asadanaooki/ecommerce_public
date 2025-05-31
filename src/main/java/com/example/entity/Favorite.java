package com.example.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Favorite {

    private String userId;
    
    private String productId;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
