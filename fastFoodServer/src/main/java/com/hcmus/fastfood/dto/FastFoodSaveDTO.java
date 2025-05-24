package com.hcmus.fastfood.dto;

import lombok.Data;

@Data
public class FastFoodSaveDTO {
    private String name;
    private String description;
    private double price;
    private String image; // base64 string
    private int quantity;
    private String categoryId;
}