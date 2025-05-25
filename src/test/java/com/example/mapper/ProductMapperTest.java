package com.example.mapper;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.dto.ProductCardDto;
import com.example.dto.ProductDetailDto;
import com.example.enums.SortType;
import com.example.mapper.ProductMapper.SearchCondition;
import com.example.testUtil.TestDataFactory;
import com.example.util.PaginationUtil;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestDataFactory.class)
class ProductMapperTest {
    /* TODO:
    ・jdbcで直接updateしてる
    ・開発用のsqlを流用してる
    */

    final int SIZE = 2;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    TestDataFactory factory;

    @Nested
    class searchProducts {
        @Nested
        class userId {
            @Test
            void searchProducts_guest() {
                List<ProductCardDto> products = productMapper.searchProducts(
                        new SearchCondition(
                                null,
                                Collections.emptyList(),
                                SortType.NEW,
                                SIZE,
                                PaginationUtil.calculateOffset(1, SIZE)));

                assertThat(products.size()).isEqualTo(2);
                assertThat(products.get(0).getProductId()).isEqualTo("1e7b4cd6-79cf-4c6f-8a8f-be1f4eda7d68");
                assertThat(products.get(0).isFav()).isFalse();
                assertThat(products.get(1).getProductId()).isEqualTo("f9c9cfb2-0893-4f1c-b508-f9e909ba5274");
            }

            @Test
            void searchProducts_login() {
                List<ProductCardDto> products = productMapper.searchProducts(
                        new SearchCondition(
                                "550e8400-e29b-41d4-a716-446655440000",
                                Collections.emptyList(),
                                SortType.NEW,
                                SIZE,
                                PaginationUtil.calculateOffset(1, SIZE)));

                assertThat(products.size()).isEqualTo(2);
                assertThat(products.get(0).getProductId()).isEqualTo("1e7b4cd6-79cf-4c6f-8a8f-be1f4eda7d68");
                assertThat(products.get(0).isFav()).isTrue();
                assertThat(products.get(1).getProductId()).isEqualTo("f9c9cfb2-0893-4f1c-b508-f9e909ba5274");
            }
        }

        @Nested
        class outOfStock {
            @Test
            void searchProducts_outOfStock() {
                // arrange
                factory.updateProductStock("1e7b4cd6-79cf-4c6f-8a8f-be1f4eda7d68", 0);

                List<ProductCardDto> products = productMapper.searchProducts(
                        new SearchCondition(
                                null,
                                Collections.emptyList(),
                                SortType.NEW,
                                SIZE,
                                PaginationUtil.calculateOffset(1, SIZE)));

                assertThat(products.size()).isEqualTo(2);

                ProductCardDto first = products.get(0);
                assertThat(first.getProductId()).isEqualTo("1e7b4cd6-79cf-4c6f-8a8f-be1f4eda7d68");
                assertThat(first.getProductName()).isEqualTo("Item19");
                assertThat(first.getPrice()).isEqualTo(750);
                assertThat(first.isOutOfStock()).isTrue();
                assertThat(first.isFav()).isFalse();

                assertThat(products.get(1).getProductId()).isEqualTo("f9c9cfb2-0893-4f1c-b508-f9e909ba5274");
            }

            @Test
            void searchProducts_inStock() {
                List<ProductCardDto> products = productMapper.searchProducts(
                        new SearchCondition(
                                null,
                                Collections.emptyList(),
                                SortType.NEW,
                                SIZE,
                                PaginationUtil.calculateOffset(1, SIZE)));

                assertThat(products.size()).isEqualTo(2);

                ProductCardDto first = products.get(0);
                assertThat(first.getProductId()).isEqualTo("1e7b4cd6-79cf-4c6f-8a8f-be1f4eda7d68");
                assertThat(first.getProductName()).isEqualTo("Item19");
                assertThat(first.getPrice()).isEqualTo(750);
                assertThat(first.isOutOfStock()).isFalse();

                assertThat(products.get(1).getProductId()).isEqualTo("f9c9cfb2-0893-4f1c-b508-f9e909ba5274");
            }
        }

        @Nested
        class keywords {
            @Test
            void searchProducts_noKeywords() {
                List<ProductCardDto> products = productMapper.searchProducts(
                        new SearchCondition(
                                null,
                                Collections.emptyList(),
                                SortType.NEW,
                                SIZE,
                                PaginationUtil.calculateOffset(2, SIZE)));

                assertThat(products.size()).isEqualTo(2);
                assertThat(products.get(0).getProductId()).isEqualTo("4a2a9e1e-4503-4cfa-ae03-3c1a5a4f2d07");
                assertThat(products.get(1).getProductId()).isEqualTo("6e1a12d8-71ab-43e6-b2fc-6ab0e5e813fd");
            }

            @Test
            void searchProducts_withKeywords() {
                List<ProductCardDto> products = productMapper.searchProducts(
                        new SearchCondition(
                                null,
                                List.of("m19", "Item5"),
                                SortType.NEW,
                                SIZE,
                                PaginationUtil.calculateOffset(1, SIZE)));

                assertThat(products.size()).isEqualTo(2);
                assertThat(products.get(0).getProductId()).isEqualTo("1e7b4cd6-79cf-4c6f-8a8f-be1f4eda7d68");
                assertThat(products.get(1).getProductId()).isEqualTo("09d5a43a-d24c-41c7-af2b-9fb7b0c9e049");
            }
        }

        @Nested
        class sort {
            @Test
            void searchProducts_newSort() {
                List<ProductCardDto> products = productMapper.searchProducts(
                        new SearchCondition(
                                null,
                                Collections.emptyList(),
                                SortType.NEW,
                                SIZE,
                                PaginationUtil.calculateOffset(1, SIZE)));

                assertThat(products.size()).isEqualTo(2);
                assertThat(products.get(0).getProductId()).isEqualTo("1e7b4cd6-79cf-4c6f-8a8f-be1f4eda7d68");
                assertThat(products.get(1).getProductId()).isEqualTo("f9c9cfb2-0893-4f1c-b508-f9e909ba5274");
            }

            @Test
            void searchProducts_highSort() {
                List<ProductCardDto> products = productMapper.searchProducts(
                        new SearchCondition(
                                null,
                                Collections.emptyList(),
                                SortType.HIGH,
                                SIZE,
                                PaginationUtil.calculateOffset(1, SIZE)));

                assertThat(products.size()).isEqualTo(2);
                assertThat(products.get(0).getProductId()).isEqualTo("f9c9cfb2-0893-4f1c-b508-f9e909ba5274");
                assertThat(products.get(1).getProductId()).isEqualTo("09d5a43a-d24c-41c7-af2b-9fb7b0c9e049");
            }

            @Test
            void searchProducts_lowSort() {
                List<ProductCardDto> products = productMapper.searchProducts(
                        new SearchCondition(
                                null,
                                Collections.emptyList(),
                                SortType.LOW,
                                SIZE,
                                PaginationUtil.calculateOffset(1, SIZE)));

                assertThat(products.size()).isEqualTo(2);
                assertThat(products.get(0).getProductId()).isEqualTo("6e1a12d8-71ab-43e6-b2fc-6ab0e5e813fd");
                assertThat(products.get(1).getProductId()).isEqualTo("1e7b4cd6-79cf-4c6f-8a8f-be1f4eda7d68");
            }
        }
    }

    @Nested
    class countProducts {
        @Test
        void countProducts_noKeyword() {
            int count = productMapper.countProducts(Collections.EMPTY_LIST);

            assertThat(count).isEqualTo(5);
        }

        @Test
        void countProducts_withKeyword() {
            int count = productMapper.countProducts(List.of("m1"));

            assertThat(count).isEqualTo(2);
        }
    }

    @Nested
    class findProductDetail {
        @ParameterizedTest
        @MethodSource("provideArguments")
        void findProductDetail_userLogin(List<Integer> ratings, BigDecimal avg) {
            String loginUid = UUID.randomUUID().toString();
            factory.createUser(loginUid);
            factory.createFavorite(loginUid, "f9c9cfb2-0893-4f1c-b508-f9e909ba5274");
            
            ratings.forEach(rating -> {
                String uid = UUID.randomUUID().toString();
                factory.createUser(uid);
                factory.createReview(uid, "f9c9cfb2-0893-4f1c-b508-f9e909ba5274", rating);
            });

            ProductDetailDto dto = productMapper.findProductDetail("f9c9cfb2-0893-4f1c-b508-f9e909ba5274",
                    loginUid);
            
            assertThat(dto.getProductId()).isEqualTo("f9c9cfb2-0893-4f1c-b508-f9e909ba5274");
            assertThat(dto.getProductName()).isEqualTo("Item18");
            assertThat(dto.getProductDescription()).isEqualTo("Item18の商品説明です。");
            assertThat(dto.getPrice()).isEqualTo(3200);
            assertThat(dto.getRatingAvg()).isEqualTo(avg);
            assertThat(dto.getReviewCount()).isEqualTo(3);
            assertThat(dto.isFav()).isTrue();
            assertThat(dto.isOutOfStock()).isFalse();
        }

        static Stream<Arguments> provideArguments() {
            return Stream.of(
                    Arguments.of(List.of(4, 4, 5), BigDecimal.valueOf(4.3)),
                    Arguments.of(List.of(4, 5, 5), BigDecimal.valueOf(4.7)));
        }
        
        @Test
        void findProductDetail_guest() {
            ProductDetailDto dto = productMapper.findProductDetail("f9c9cfb2-0893-4f1c-b508-f9e909ba5274",
                    null);
            
            assertThat(dto.getProductId()).isEqualTo("f9c9cfb2-0893-4f1c-b508-f9e909ba5274");
            assertThat(dto.getProductName()).isEqualTo("Item18");
            assertThat(dto.getProductDescription()).isEqualTo("Item18の商品説明です。");
            assertThat(dto.getPrice()).isEqualTo(3200);
            assertThat(dto.getRatingAvg()).isEqualTo(BigDecimal.valueOf(0.0));
            assertThat(dto.getReviewCount()).isEqualTo(0);
            assertThat(dto.isFav()).isFalse();
            assertThat(dto.isOutOfStock()).isFalse();
        }
    }
}
