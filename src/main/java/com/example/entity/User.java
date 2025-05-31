package com.example.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class User {

    private String userId;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
