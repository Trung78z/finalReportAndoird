package com.hcmus.fastfood.dto;

import com.hcmus.fastfood.model.FastFood;

public class CartFoodDTO {
    private FastFood food;
    private int quantity;
    private boolean active;

    // Constructors
    public CartFoodDTO(FastFood food, int quantity, boolean active) {
        this.food = food;
        this.quantity = quantity;
        this.active = active;
    }

    // Getters and setters
    public FastFood getFood() { return food; }
    public void setFood(FastFood food) { this.food = food; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}