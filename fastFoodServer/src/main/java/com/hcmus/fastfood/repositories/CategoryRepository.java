package com.hcmus.fastfood.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmus.fastfood.model.Category;

public interface CategoryRepository  extends JpaRepository<Category, String> {
    
}
