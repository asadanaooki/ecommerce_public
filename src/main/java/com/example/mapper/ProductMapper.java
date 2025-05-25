package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.dto.ProductCardDto;
import com.example.dto.ProductDetailDto;
import com.example.enums.SortType;

@Mapper
public interface ProductMapper {
// TODO:
    // searchProductsで外部結合とサブクエリの速度比較
    
    List<ProductCardDto> searchProducts(SearchCondition sc);

    int countProducts(List<String> keywords);
    
    ProductDetailDto findProductDetail(String productId, String userId);
    
    record SearchCondition(String userId,
            List<String> keywords,
            SortType sort,
            int size,
            int offset) {
    }
}
