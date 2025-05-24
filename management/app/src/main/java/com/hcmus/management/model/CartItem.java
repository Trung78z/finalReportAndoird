package com.hcmus.management.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItem implements Serializable {
    private Integer id;
    private int quantity;
    private FoodItem food;

    public CartItem(Integer id, int quantity, FoodItem food) {
        this.id = id;
        this.quantity = quantity;
        this.food = food;
    }
}