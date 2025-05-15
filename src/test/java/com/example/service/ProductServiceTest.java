package com.example.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.dto.ProductCardDto;
import com.example.dto.ProductListDto;
import com.example.enums.SortType;
import com.example.mapper.ProductMapper;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductMapper productMapper;
    
    @Autowired
    ProductService productService;
    
    @Value("${settings.product.size}")
    int pageSize;
    
    @Value("${settings.product.page-nav-radius}")
    int radius;
    @Test
    void searchProducts() {
        ProductListDto dto = productService.searchProducts(1, SortType.NEW, List.of("tem"));
        
        assertThat(dto.getProducts()).hasSize(pageSize)
        .extracting(ProductCardDto::getProductId)
        .containsExactly("1e7b4cd6-79cf-4c6f-8a8f-be1f4eda7d68","f9c9cfb2-0893-4f1c-b508-f9e909ba5274");
        
        assertThat(dto.getTotalPage()).isEqualTo(3);
        assertThat(dto.getPageNumbers()).isEqualTo(List.of(1,2,3));
    }

}
