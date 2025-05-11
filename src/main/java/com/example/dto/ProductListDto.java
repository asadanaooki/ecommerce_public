package com.example.dto;

import java.util.List;

import com.example.enums.SortType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductListDto {

   private List<ProductCardDto> products;
    
   private int currentPage;
   
   private int totalPages;
   
   private List<Integer> pageNumbers;
   
   private SortType sort;
   
   private List<String> keywords;
}
