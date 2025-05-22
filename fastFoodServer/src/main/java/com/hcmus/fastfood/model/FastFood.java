package com.hcmus.fastfood.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "fast_food")
@Data
public class FastFood {
    @Id
    private String id; // Manually set UUID (e.g., UUID.randomUUID().toString())

    private String name;
    private String description;
    private double price;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}