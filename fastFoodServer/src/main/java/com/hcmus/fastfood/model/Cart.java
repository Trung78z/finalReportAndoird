package com.hcmus.fastfood.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "cart")
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id; // Manually set UUID (e.g., UUID.randomUUID().toString())
    private String userId;
    private String foodId;
    private int quantity;
    private boolean isActive;
}
