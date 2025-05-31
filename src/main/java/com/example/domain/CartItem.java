package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CartItem {

    private final String productId;
    
    private int qty;
    
    private int lastPriceInc;
    
    public void addQty(int add) {
        this.qty = Math.min(this.qty + add, 20);
    }
}
