package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.component.TaxCalculator;
import com.example.domain.Cart;
import com.example.dto.ProductCardDto;
import com.example.dto.ProductDetailDto;
import com.example.dto.ProductListDto;
import com.example.entity.Product;
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
    ・毎回手動で税込み価格計算するか？
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

        List<ProductCardDto> products = productMapper
                .searchProducts(new ProductMapper.SearchCondition(userId, keywords, sort, pageSize, offset));
        // 税込み価格へ変換
        products.forEach(p -> p.setPrice(taxCalculator.calculatePriceIncludingTax(p.getPrice())));

        int totalCount = productMapper.countProducts(keywords);

        int totalPage = PaginationUtil.calculateTotalPage(totalCount, pageSize);
        List<Integer> pageNumbers = PaginationUtil.createPageNumbers(page, totalPage, radius);

        return new ProductListDto(products, totalPage, pageNumbers);
    }

    public void addFavorite(String productId) {
        // SpringSecurityを通過してるため、ログイン済み
        // 未ログインだとログイン画面へリダイレクトされる
        // TODO: getNameだとemailがかえるため、getUserIdに変更する
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        favoriteMapper.addFavorite(userId, productId);
    }

    public void deleteFavorite(String productId) {
        // SpringSecurityを通過してるため、ログイン済み
        // 未ログインだとログイン画面へリダイレクトされる
        // TODO: getNameだとemailがかえるため、getUserIdに変更する
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        favoriteMapper.deleteFavorite(userId, productId);
    }
    
    public ProductDetailDto getProductDetail(String productId) {
        // TODO: getNameだとemailがかえるため、getUserIdに変更する
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth == null ? null : auth.getName();
        
        ProductDetailDto dto = productMapper.findProductDetail(productId, userId);
        dto.setPrice(taxCalculator.calculatePriceIncludingTax(dto.getPrice()));
        
        return dto;
    }
    
    public void addToCart(Cart cart, String productId, int qty) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        
        cart.merge(productId, qty, taxCalculator.calculatePriceIncludingTax(product.getPrice()));
    }
}
