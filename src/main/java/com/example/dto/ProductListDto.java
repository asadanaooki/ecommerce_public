package com.example.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductListDto {

   private List<ProductCardDto> products;
    
   private int totalPage;
   
   private List<Integer> pageNumbers;
}
