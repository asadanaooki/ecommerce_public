package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.dto.ProductCardDto;
import com.example.enums.SortType;

@Mapper
public interface ProductMapper {

    List<ProductCardDto> searchProducts(List<String> keywords, SortType sort, int size, int offset);
    
    int countProducts(List<String> keywords);
}
