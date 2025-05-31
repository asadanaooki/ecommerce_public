package com.example.mapper;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FavoriteMapperTest {
    
    @Autowired
    FavoriteMapper favoriteMapper;
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void addFavorite() {
        int row = favoriteMapper.addFavorite("550e8400-e29b-41d4-a716-446655440000",
                "09d5a43a-d24c-41c7-af2b-9fb7b0c9e049");
        boolean exists = jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT 1 FROM favorite WHERE user_id = ? AND product_id = ?)",
                Boolean.class,
                "550e8400-e29b-41d4-a716-446655440000",
                "09d5a43a-d24c-41c7-af2b-9fb7b0c9e049");
        
        assertThat(row).isEqualTo(1);
        assertThat(exists).isTrue();
    }
    
    @Test
    void deleteFavorite() {
        int row = favoriteMapper.deleteFavorite("550e8400-e29b-41d4-a716-446655440000",
                "1e7b4cd6-79cf-4c6f-8a8f-be1f4eda7d68");
        boolean exists = jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT 1 FROM favorite WHERE user_id = ? AND product_id = ?)",
                Boolean.class,
                "550e8400-e29b-41d4-a716-446655440000",
                "1e7b4cd6-79cf-4c6f-8a8f-be1f4eda7d68");
        
        assertThat(row).isEqualTo(1);
        assertThat(exists).isFalse();
    }

}
