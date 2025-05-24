package com.hcmus.fastfood.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcmus.fastfood.dto.FastFoodSaveDTO;
import com.hcmus.fastfood.model.Category;
import com.hcmus.fastfood.model.FastFood;
import com.hcmus.fastfood.repositories.CategoryRepository;
import com.hcmus.fastfood.repositories.FastFoodRepository;

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
        fastFood.setCategory(category);

        // Save image to resources/static/images/
        if (dto.getImage() != null && !dto.getImage().isBlank()) {
            try {
                String fileName = "food_" + System.currentTimeMillis() + ".png";
                String folderPath = "src/main/resources/static/images";
                File folder = new File(folderPath);
                if (!folder.exists())
                    folder.mkdirs();

                String filePath = folderPath + "/" + fileName;
                byte[] imageBytes = Base64.getDecoder().decode(dto.getImage());
                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    fos.write(imageBytes);
                }
                // Set the URL (adjust if you serve static files differently)
                fastFood.setImageUrl("/images/" + fileName);
            } catch (IOException | IllegalArgumentException e) {
                throw new RuntimeException("Failed to save image", e);
            }
        }

        return fastFoodRepository.save(fastFood);
    }

    public List<FastFood> getAllFastFood() {
        return fastFoodRepository.findAll();
    }

    public FastFood updateFastFood(String id, FastFoodSaveDTO dto) {
        FastFood fastFood = fastFoodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fast food not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        fastFood.setName(dto.getName());
        fastFood.setDescription(dto.getDescription());
        fastFood.setQuantity(dto.getQuantity());
        fastFood.setPrice(dto.getPrice());
        fastFood.setCategory(category);
        return fastFoodRepository.save(fastFood);
    }

    public void deleteFastFood(String id) {
        FastFood fastFood = fastFoodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fast food not found"));
        fastFoodRepository.delete(fastFood);
    }
}