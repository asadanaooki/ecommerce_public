package com.example.domain;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class Cart {

    private final Map<String, CartItem> items = new HashMap<String, CartItem>();
    
    public void merge(String productId, int add, int price) {
        CartItem item = items.get(productId);
        
        if(item == null) {
            item = new CartItem(productId, 0, 0);
            items.put(productId, item);
        }
        item.addQty(add);
        item.setLastPriceInc(price);
    }
}
