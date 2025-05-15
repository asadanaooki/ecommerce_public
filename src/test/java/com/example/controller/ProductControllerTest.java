package com.example.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.controller.ProductController.SearchParam;
import com.example.dto.ProductListDto;
import com.example.enums.SortType;
import com.example.service.ProductService;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    /*    TODO
    ・パラメータテストこれだけでいいのか？
    */

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProductService productService;

    @ParameterizedTest
    @MethodSource("provideArguments")
    void searchProducts(String page, String sort, String q, SearchParam expected) throws Exception {
        doReturn(new ProductListDto()).when(productService).searchProducts(anyInt(), any(), anyList());
        ResultMatcher matcher = result ->{
            if (q != null) {
                MockMvcResultMatchers.model().attributeExists("keywords").match(result);
            }else {
                MockMvcResultMatchers.model().attributeDoesNotExist("keywords").match(result);
            }
        };
        
        MockHttpServletRequestBuilder req = get("/product");
        if (page != null) {
            req.param("page", page);
        }
        req.param("sort", sort);
        if (q != null) {
            req.param("q", q);
        }

        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(view().name("productList"))
                .andExpect(matcher);

        verify(productService).searchProducts(expected.page(), expected.sort(), expected.keywords());
    }

    static Stream<Arguments> provideArguments() {
        return Stream.of(
                // page
                Arguments.of("2", "NEW", null, new SearchParam(2, SortType.NEW, Collections.emptyList())),
                Arguments.of("1", "NEW", null, new SearchParam(1, SortType.NEW, Collections.emptyList())),
                Arguments.of("0", "NEW", null, new SearchParam(1, SortType.NEW, Collections.emptyList())),
                Arguments.of("test", "NEW", null, new SearchParam(1, SortType.NEW, Collections.emptyList())),

                // sort
                Arguments.of(null, "NEW", null, new SearchParam(1, SortType.NEW, Collections.emptyList())),
                Arguments.of(null, "HIGH", null, new SearchParam(1, SortType.HIGH, Collections.emptyList())),
                Arguments.of(null, "LOW", null, new SearchParam(1, SortType.LOW, Collections.emptyList())),
                Arguments.of(null, "test", null, new SearchParam(1, SortType.NEW, Collections.emptyList())),

                // keywords
                Arguments.of(null, "NEW", null, new SearchParam(1, SortType.NEW, Collections.emptyList())),
                Arguments.of(null, "NEW", "test 　りんご　バナna  ",
                        new SearchParam(1, SortType.NEW, List.of("test", "りんご", "バナna"))));
    }

}
