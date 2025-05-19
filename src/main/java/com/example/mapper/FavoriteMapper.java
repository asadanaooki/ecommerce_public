package com.example.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FavoriteMapper {

    int addFavorite(String userId, String productId);
    
    int deleteFavorite(String userId, String productId);
}
