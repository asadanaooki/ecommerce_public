package com.example.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dto.ProductListDto;
import com.example.enums.SortType;
import com.example.service.ProductService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class ProductController {
    // TODO
    //・トップページを/productにするか？
    //・パラメータの受け取りを個別に書いてるが、Formとかで受け取るべきか？
    //・splitの正規表現で[\\s　]+だと ブラウザの &nbsp; や Word の “改行したくない空き” などをコピー時に対応していない
    // ・パラメータ補正をtry-catchで行ってるが、パフォーマンス落ちるか？
    
    private final ProductService productService;

    @GetMapping("/product")
    public String searchProducts(
            @RequestParam(defaultValue = "1") String page,
            @RequestParam(defaultValue = "NEW") String sort,
            @RequestParam(required = false) String q,
            Model model) {
        SearchParam param = adjustSearchParam(page, sort, q);

        ProductListDto dto = productService.searchProducts(param.page, param.sort, param.keywords);
        model.addAttribute("dto", dto);
        model.addAttribute("currentPage", param.page);
        model.addAttribute("sort", param.sort);
        model.addAttribute("keywords", q);
        
        return "productList";
    }

    private SearchParam adjustSearchParam(String page, String sort, String q) {
        int pageNum;
        try {
            pageNum = Integer.parseInt(page);
            if (pageNum < 1) {
                pageNum = 1;
            }
        } catch (NumberFormatException e) {
            pageNum = 1;
        }

        SortType sortType;
        try {
            sortType = SortType.valueOf(sort.toUpperCase());
        } catch (IllegalArgumentException e) {
            sortType = SortType.NEW; // フォールバック
        }

        String raw = Optional.ofNullable(q).orElse("").trim();
        List<String> keywords = raw.isEmpty()
                ? Collections.emptyList()
                : Arrays.stream(raw.split("[\\s　]+"))
                        .toList();

        return new SearchParam(pageNum, sortType, keywords);
    }

    record SearchParam(int page, SortType sort, List<String> keywords) {
    }
}
