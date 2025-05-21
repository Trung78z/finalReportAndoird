package com.hcmus.management.model;

import lombok.Data;

@Data
public class FoodItem {
    private String id;
    private String name;
    private double price;
    private int quantity;
    private boolean isChecked;
    private int imageResId;
    private double unitPrice;
    public FoodItem(String name, double price, int quantity, boolean isChecked, int imageResId) {
        this.name = name;
        this.price = price;
        this.unitPrice = price;
        this.quantity = quantity;
        this.isChecked = isChecked;
        this.imageResId = imageResId;
    }
    public double getPrice() {
        return unitPrice * quantity;
    }

}
