package com.hcmus.fastfood.dto;

import com.hcmus.fastfood.model.FastFood;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartFoodDTO {
    private Integer id;
    private FastFood food;
    private int quantity;
    private boolean active;

    // Constructors
    public CartFoodDTO(Integer id, FastFood food, int quantity, boolean active) {
        this.id = id;
        this.food = food;
        this.quantity = quantity;
        this.active = active;
    }
}