package com.hcmus.fastfood.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "fast_food")
@Data
public class FastFood {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String description;
    private double price;
    // image with base64 format
    @Column(columnDefinition = "TEXT")
    @Lob
    private String image;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
}
