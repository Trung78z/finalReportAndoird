package com.hcmus.fastfood.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fast_food")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FastFood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Manually set UUID (e.g., UUID.randomUUID().toString())

    private String name;
    private String description;
    private double price;

    private String imageUrl;

    @Builder.Default
    private int quantity = 1;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}