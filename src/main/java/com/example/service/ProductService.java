package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.component.TaxCalculator;
import com.example.dto.ProductCardDto;
import com.example.dto.ProductListDto;
import com.example.enums.SortType;
import com.example.mapper.FavoriteMapper;
import com.example.mapper.ProductMapper;
import com.example.util.PaginationUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    /*
     *     // TODO:
    ・総件数　XX件～XX件目表示を返す
    ・お気に入りフラグ取得するのに、JOINか2回にわけるか比較
    */

    private final ProductMapper productMapper;
    
    private final FavoriteMapper favoriteMapper;
    
    @Value("${settings.product.size}")
    private int pageSize;
    
    @Value("${settings.product.page-nav-radius}")
    private int radius;
    
    private final TaxCalculator taxCalculator;
    
    public ProductListDto searchProducts(int page, SortType sort, List<String> keywords) {
        int offset = PaginationUtil.calculateOffset(page, pageSize);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth == null ? null : auth.getName();
        
        List<ProductCardDto> products = productMapper.searchProducts(new ProductMapper.SearchCondition
                (userId, keywords, sort, pageSize, offset));
        // 税込み価格へ変換
        products.forEach(p ->
        p.setPrice(taxCalculator.calculatePriceIncludingTax(p.getPrice())));
        
        int totalCount = productMapper.countProducts(keywords);
        
        int totalPage = PaginationUtil.calculateTotalPage(totalCount, pageSize);
        List<Integer> pageNumbers = PaginationUtil.createPageNumbers(page, totalPage, radius);
        
        return new ProductListDto(products, totalPage, pageNumbers);
    }
    
    public void addFavorite(String productId) {
        // SpringSecurityを通過してるため、ログイン済み
        // 未ログインだとログイン画面へリダイレクトされる
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        
        favoriteMapper.addFavorite(userId, productId);
    }
    
    public void deleteFavorite(String productId) {
        // SpringSecurityを通過してるため、ログイン済み
     // 未ログインだとログイン画面へリダイレクトされる
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        
        favoriteMapper.deleteFavorite(userId, productId);
    }
}
