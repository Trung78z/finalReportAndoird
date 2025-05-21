package com.hcmus.management.model;

import lombok.Data;

@Data
public class CardItem {
    private String id;
    private String name;
    private double price;
    private int imageResId;

    public CardItem(String id, String name, double price, int imageResId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
    }
}
