package com.example.testUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestDataFactory {

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    public void updateProductStock(String productId, int stock) {
        jdbcTemplate.update(
                "UPDATE product SET stock = ? WHERE product_id = ?",
                stock,
                productId);
    }

    public void createReview(String userId, String productId, int rating) {
        jdbcTemplate.update(
                "INSERT INTO review (user_id, product_id, rating) VALUES (?, ?, ?)",
                userId, productId, rating);
    }

    public void createUser(String userId) {
        jdbcTemplate.update(
                "INSERT INTO user (user_id) VALUES (?)", userId);
    }
    
    public void createFavorite(String userId, String productId) {
        jdbcTemplate.update(
                "INSERT INTO favorite (user_id, product_id) VALUES (?, ?)", userId, productId);
    }

}
