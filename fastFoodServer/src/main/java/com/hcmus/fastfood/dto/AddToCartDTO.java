package com.hcmus.fastfood.dto;

import lombok.Data;

@Data
public class AddToCartDTO {
    private String userId;
    private String foodId;
    private int quantity;
}