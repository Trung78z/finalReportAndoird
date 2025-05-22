package com.hcmus.fastfood.controller;

import com.hcmus.fastfood.dto.CategorySaveDTO;
import com.hcmus.fastfood.model.Category;
import com.hcmus.fastfood.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Category saveCategory(@RequestBody CategorySaveDTO dto) {
        return categoryService.saveCategory(dto);
    }
}
