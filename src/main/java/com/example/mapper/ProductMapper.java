package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.dto.ProductCardDto;
import com.example.enums.SortType;

@Mapper
public interface ProductMapper {
// TODO:
    // searchProductsで外部結合かサブクエリの速度比較
    
    List<ProductCardDto> searchProducts(SearchCondition sc);

    int countProducts(List<String> keywords);

    record SearchCondition(String userId,
            List<String> keywords,
            SortType sort,
            int size,
            int offset) {
    }
}
