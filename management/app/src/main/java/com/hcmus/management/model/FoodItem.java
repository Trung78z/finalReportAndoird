package com.hcmus.management.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class FoodItem implements Serializable {
    private String id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String imageUrl;
    private int categoryId;
    private boolean isChecked = false;
    
    private Integer cartId;

    public FoodItem(String id, String name, String description, double price, int quantity, String imageUrl, int categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

}
