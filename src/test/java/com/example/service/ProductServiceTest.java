package com.example.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.web.server.ResponseStatusException;

import com.example.domain.Cart;
import com.example.dto.ProductCardDto;
import com.example.dto.ProductDetailDto;
import com.example.dto.ProductListDto;
import com.example.entity.Product;
import com.example.enums.SortType;
import com.example.mapper.FavoriteMapper;
import com.example.mapper.ProductMapper;

@SpringBootTest
class ProductServiceTest {

    @MockitoSpyBean
    ProductMapper productMapper;

    @MockitoBean
    FavoriteMapper favoriteMapper;

    @Autowired
    ProductService productService;

    @Value("${settings.product.size}")
    int pageSize;

    @Value("${settings.product.page-nav-radius}")
    int radius;

    @Test
    void searchProducts() {
        ProductListDto dto = productService.searchProducts(1, SortType.NEW, List.of("tem"));

        assertThat(dto.getProducts()).hasSize(pageSize);

        ProductCardDto first = dto.getProducts().get(0);
        assertThat(first.getProductId()).isEqualTo("1e7b4cd6-79cf-4c6f-8a8f-be1f4eda7d68");
        assertThat(first.getProductName()).isEqualTo("Item19");
        assertThat(first.getPrice()).isEqualTo(825);

        assertThat(dto.getProducts().get(1).getProductId()).isEqualTo("f9c9cfb2-0893-4f1c-b508-f9e909ba5274");

        assertThat(dto.getTotalPage()).isEqualTo(3);
        assertThat(dto.getPageNumbers()).isEqualTo(List.of(1, 2, 3));
    }

    @Nested
    @WithMockUser(username = "550e8400-e29b-41d4-a716-446655420000")
    class favorite {
        String productId = "97113c2c-719a-490c-9979-144d92905c31";

        @Test
        void addFavorite() {
            productService.addFavorite(productId);

            verify(favoriteMapper).addFavorite("550e8400-e29b-41d4-a716-446655420000",
                    "97113c2c-719a-490c-9979-144d92905c31");
        }

        @Test
        void deleteFavorite() {
            productService.deleteFavorite(productId);

            verify(favoriteMapper).deleteFavorite("550e8400-e29b-41d4-a716-446655420000",
                    "97113c2c-719a-490c-9979-144d92905c31");
        }
    }

    @Nested
    class getProductDetail {
        ProductDetailDto ret;
        
        ProductDetailDto expected;
        
        @BeforeEach
        void setup() {
            ret = new ProductDetailDto(
                    "dummy", "ダミー", "説明", 800, false, BigDecimal.ZERO, 0, false);
            
            expected = new ProductDetailDto(
                    "dummy", "ダミー", "説明", 880, false, BigDecimal.ZERO, 0, false);
        }
        
        @Test
        void getProductDetail_guest() {
            doReturn(ret).when(productMapper)
            .findProductDetail("1e7b4cd6-79cf-4c6f-8a8f-be1f4eda7d68", null);

            ProductDetailDto actual = productService.getProductDetail("1e7b4cd6-79cf-4c6f-8a8f-be1f4eda7d68");

            assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
        }
        
        @Test
        @WithMockUser(username = "550e8400-e29b-41d4-a716-446655420000")
        void getProductDetail_userLogin() {
            doReturn(ret).when(productMapper)
            .findProductDetail("1e7b4cd6-79cf-4c6f-8a8f-be1f4eda7d68", "550e8400-e29b-41d4-a716-446655420000");

            ProductDetailDto actual = productService.getProductDetail("1e7b4cd6-79cf-4c6f-8a8f-be1f4eda7d68");

            assertThat(actual).usingRecursiveAssertion().isEqualTo(expected);
        }
    }

    @Nested
    class addToCart{
        
        @Test
        void addToCart_productNotFound() {
            Cart cart = mock(Cart.class);
            String productId = "Invalid";
            doReturn(null).when(productMapper).selectById(productId);
            
            assertThatThrownBy(() -> productService.addToCart(cart, productId, 3))
            .isInstanceOf(ResponseStatusException.class)
            .satisfies(ex -> assertThat(((ResponseStatusException)ex).getStatusCode())
                    .isEqualTo(HttpStatus.NOT_FOUND));
            
            verify(cart, never()).merge(anyString(), anyInt(), anyInt());
        }
        
        @Test
        void addToCart_newItem() {
            String productId = UUID.randomUUID().toString();
            Product p = new Product();
            p.setPrice(1000);
            Cart cart = new Cart();
            doReturn(p).when(productMapper).selectById(productId);
            
            productService.addToCart(cart, productId, 3);
            
            assertThat(cart.getItems().values()).singleElement()
            .satisfies(ci ->{
                assertThat(ci.getProductId()).isEqualTo(productId);
                assertThat(ci.getQty()).isEqualTo(3);
                assertThat(ci.getLastPriceInc()).isEqualTo(1100);
            });
        }
        
        @Test
        void addToCart_existingItem() {
            String productId = UUID.randomUUID().toString();
            Product p = new Product();
            p.setPrice(1200);
            Cart cart = new Cart();
            doReturn(p).when(productMapper).selectById(productId);
            cart.merge(productId, 5, 1000);
            
            productService.addToCart(cart, productId, 3);
            
            assertThat(cart.getItems().values()).singleElement()
            .satisfies(ci ->{
                assertThat(ci.getProductId()).isEqualTo(productId);
                assertThat(ci.getQty()).isEqualTo(8);
                assertThat(ci.getLastPriceInc()).isEqualTo(1320);
            });
        }
        
        @Test
        void addToCart_qtyCapped() {
            String productId = UUID.randomUUID().toString();
            Product p = new Product();
            p.setPrice(1200);
            Cart cart = new Cart();
            doReturn(p).when(productMapper).selectById(productId);
            cart.merge(productId, 15, 1200);
            
            productService.addToCart(cart, productId, 10);
            
            assertThat(cart.getItems().values()).singleElement()
            .satisfies(ci ->{
                assertThat(ci.getProductId()).isEqualTo(productId);
                assertThat(ci.getQty()).isEqualTo(20);
                assertThat(ci.getLastPriceInc()).isEqualTo(1320);
            });
        }
    }
}
