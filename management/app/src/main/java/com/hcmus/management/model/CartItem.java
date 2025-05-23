package com.hcmus.management.model;

import lombok.Data;

@Data
public class CartItem {
    private String id;
    private int quantity;
    private FoodItem food;

    public CartItem(String id, int quantity, FoodItem food) {
        this.id = id;
        this.quantity = quantity;
        this.food = food;
    }
}