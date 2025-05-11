package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.dto.ProductCardDto;
import com.example.dto.ProductListDto;
import com.example.enums.SortType;
import com.example.mapper.ProductMapper;
import com.example.util.PaginationUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;
    
    @Value("${settings.product.size}")
    private int pageSize;
    
    @Value("${settings.product.page-nav-radius}")
    private int radius;
    
    public ProductListDto searchProducts(int page, SortType sort, List<String> keywords) {
        int offset = PaginationUtil.calculateOffset(page, pageSize);
        List<ProductCardDto> products = productMapper.searchProducts(keywords, sort, pageSize, offset);
        int totalCount = productMapper.countProducts(keywords);
        
        int totalPage = PaginationUtil.calculateTotalPage(totalCount, pageSize);
        List<Integer> pageNumbers = PaginationUtil.createPageNumbers(page, totalPage, radius);
        
        return new ProductListDto(products, page, totalPage, pageNumbers, sort, keywords);
    }
}
