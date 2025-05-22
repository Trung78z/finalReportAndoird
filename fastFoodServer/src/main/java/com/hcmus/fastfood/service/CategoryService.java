package com.hcmus.fastfood.service;


import com.hcmus.fastfood.dto.CategorySaveDTO;
import com.hcmus.fastfood.model.Category;
import com.hcmus.fastfood.repositories.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category saveCategory(CategorySaveDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        return categoryRepository.save(category);
    }
}