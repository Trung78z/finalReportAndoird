package com.hcmus.fastfood.service;

import com.hcmus.fastfood.dto.FastFoodSaveDTO;
import com.hcmus.fastfood.model.Category;
import com.hcmus.fastfood.model.FastFood;
import com.hcmus.fastfood.repositories.CategoryRepository;
import com.hcmus.fastfood.repositories.FastFoodRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FastFoodService {

    @Autowired
    private FastFoodRepository fastFoodRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public FastFood saveFastFood(FastFoodSaveDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        FastFood fastFood = new FastFood();
        fastFood.setName(dto.getName());
        fastFood.setDescription(dto.getDescription());
        fastFood.setPrice(dto.getPrice());
        fastFood.setImage(dto.getImage());
        fastFood.setCategory(category);

        return fastFoodRepository.save(fastFood);
    }
}