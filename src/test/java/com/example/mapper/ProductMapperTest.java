package com.example.mapper;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.dto.ProductCardDto;
import com.example.enums.SortType;
import com.example.mapper.ProductMapper.SearchCondition;
import com.example.util.PaginationUtil;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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

    @Nested
    class searchProducts {
        @Nested
        class userId{
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
                jdbcTemplate.update(
                        "UPDATE product SET stock = ? WHERE product_id = ?",
                        0,
                        "1e7b4cd6-79cf-4c6f-8a8f-be1f4eda7d68");

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

}
